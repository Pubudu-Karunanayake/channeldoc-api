package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.message.SessionCancellationMessage;
import com.medisync.channeldoc_api.dto.response.DailySessionResponseDto;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.messaging.producer.SessionCancellationProducer;
import com.medisync.channeldoc_api.model.Appointment;
import com.medisync.channeldoc_api.model.DailySession;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.SessionStatus;
import com.medisync.channeldoc_api.repository.AppointmentRepository;
import com.medisync.channeldoc_api.repository.DailySessionRepository;
import com.medisync.channeldoc_api.service.DailySessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailySessionServiceImpl implements DailySessionService {

    private final DailySessionRepository dailySessionRepository;
    private final AppointmentRepository appointmentRepository;
    private final com.medisync.channeldoc_api.repository.TimeSlotRepository timeSlotRepository;
    private final SessionCancellationProducer sessionCancellationProducer;

    @Override
    public List<DailySessionResponseDto> getDailySessions(LocalDate date, Long masterScheduleId, User user) {
        Hospital hospital = user.getHospital();
        if (hospital == null) {
            throw new IllegalArgumentException("User does not belong to any hospital");
        }

        if (masterScheduleId != null) {
            Optional<DailySession> session = dailySessionRepository
                    .findByMasterScheduleIdAndSessionDateAndHospital(masterScheduleId, date, hospital);
            return session.map(s -> List.of(mapToDto(s))).orElse(List.of());
        }

        List<DailySession> sessions = dailySessionRepository.findByHospitalAndSessionDate(hospital, date);
        return sessions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Cancels a DailySession and all associated active appointments.
     *
     * <p><b>Pitfall #2 Fix (Transaction Safety):</b> RabbitMQ message publishing is wrapped
     * in a try-catch block. If the broker is down, the DB transaction still commits —
     * the session and appointments are cancelled regardless of message delivery.
     * Email notification is best-effort.</p>
     *
     * <p><b>Pitfall #3 Fix (Dirty Checking):</b> Appointment statuses are updated via
     * setter methods only. Since this method is {@code @Transactional}, Hibernate
     * automatically detects dirty entities and flushes all changes in a single batch
     * at transaction commit — no explicit {@code save()} calls needed.</p>
     */
    @Override
    @Transactional
    public DailySessionResponseDto cancelDailySession(Long sessionId, User user) {
        // 1. Fetch and validate the DailySession
        DailySession dailySession = dailySessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily session not found with id: " + sessionId));

        // 2. Verify the session belongs to the logged-in user's hospital
        Hospital userHospital = user.getHospital();
        if (userHospital == null || !userHospital.getId().equals(dailySession.getHospital().getId())) {
            throw new AccessDeniedException("You do not have permission to cancel sessions for this hospital");
        }

        // 3. Validate the session is currently ACTIVE
        if (dailySession.getStatus() == SessionStatus.CANCELLED) {
            throw new IllegalStateException("Daily session with id " + sessionId + " is already cancelled");
        }

        // 4. Cancel the session (dirty checking — no explicit save needed)
        dailySession.setStatus(SessionStatus.CANCELLED);
        log.info("DailySession [{}] marked as CANCELLED", sessionId);

        // 5. Find and cancel all active appointments for this session
        //    Pitfall #3 Fix: setter only, Hibernate dirty checking handles the UPDATE
        List<Appointment> activeAppointments = appointmentRepository.findActiveAppointmentsByDailySessionId(sessionId);
        for (Appointment appointment : activeAppointments) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
        }
        log.info("Cancelled {} active appointments for DailySession [{}]", activeAppointments.size(), sessionId);

        // 6. Publish cancellation notifications via RabbitMQ
        //    Pitfall #2 Fix: try-catch ensures DB transaction commits even if broker is down
        try {
            for (Appointment appointment : activeAppointments) {
                SessionCancellationMessage message = SessionCancellationMessage.builder()
                        .patientEmail(appointment.getPatient().getEmail())
                        .patientName(appointment.getPatient().getFullName())
                        .doctorName(appointment.getDoctor().getUser().getFullName())
                        .hospitalName(appointment.getHospital().getName())
                        .sessionDate(dailySession.getSessionDate())
                        .appointmentNumber(appointment.getAppointmentNumber())
                        .build();

                sessionCancellationProducer.sendCancellationNotification(message);
            }
        } catch (Exception e) {
            log.error("Failed to publish cancellation messages to RabbitMQ for DailySession [{}]. " +
                    "DB transaction will still commit. Error: {}", sessionId, e.getMessage(), e);
        }

        return mapToDto(dailySession);
    }

    private DailySessionResponseDto mapToDto(DailySession session) {
        return DailySessionResponseDto.builder()
                .id(session.getId())
                .hospitalId(session.getHospital() != null ? session.getHospital().getId() : null)
                .doctorId(session.getDoctor() != null ? session.getDoctor().getId() : null)
                .masterScheduleId(session.getMasterSchedule() != null ? session.getMasterSchedule().getId() : null)
                .sessionDate(session.getSessionDate())
                .status(session.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.medisync.channeldoc_api.dto.response.TimeSlotResponseDto> getTimeSlotsForSession(Long sessionId, User user) {
        DailySession dailySession = dailySessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Daily session not found with id: " + sessionId));

        if (user.getRoles().contains(com.medisync.channeldoc_api.model.enums.UserRole.ROLE_HOSPITAL_ADMIN)) {
            Hospital userHospital = user.getHospital();
            if (userHospital == null || !userHospital.getId().equals(dailySession.getHospital().getId())) {
                throw new AccessDeniedException("You do not have permission to access time slots for this hospital's session");
            }
        }

        List<com.medisync.channeldoc_api.model.TimeSlot> timeSlots = timeSlotRepository.findByDailySessionIdOrderBySlotTimeAsc(sessionId);

        return timeSlots.stream().map(ts -> com.medisync.channeldoc_api.dto.response.TimeSlotResponseDto.builder()
                .id(ts.getId())
                .slotTime(ts.getSlotTime())
                .status(Boolean.TRUE.equals(ts.getIsBooked()) ? "BOOKED" : "AVAILABLE")
                .build()
        ).collect(Collectors.toList());
    }
}

