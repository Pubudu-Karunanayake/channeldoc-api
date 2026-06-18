package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.AppointmentBookingRequestDto;
import com.medisync.channeldoc_api.dto.response.AppointmentResponseDto;
import com.medisync.channeldoc_api.event.AppointmentBookedEvent;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.Appointment;
import com.medisync.channeldoc_api.model.DailySession;
import com.medisync.channeldoc_api.model.MasterSchedule;
import com.medisync.channeldoc_api.model.TimeSlot;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.PaymentStatus;
import com.medisync.channeldoc_api.repository.AppointmentRepository;
import com.medisync.channeldoc_api.repository.TimeSlotRepository;
import com.medisync.channeldoc_api.service.AppointmentBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentBookingServiceImpl implements AppointmentBookingService {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public AppointmentResponseDto bookAppointment(AppointmentBookingRequestDto request, User user) {
        // 1. Find and validate the time slot
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Time slot not found with id: " + request.getTimeSlotId()));

        if (Boolean.TRUE.equals(timeSlot.getIsBooked())) {
            throw new IllegalArgumentException("This time slot is already booked. Please select a different slot.");
        }

        // 2. Mark the slot as booked (optimistic lock via @Version)
        timeSlot.setIsBooked(true);
        timeSlotRepository.save(timeSlot);

        // 3. Derive data from the entity chain: TimeSlot → DailySession → MasterSchedule
        DailySession dailySession = timeSlot.getDailySession();
        MasterSchedule masterSchedule = dailySession.getMasterSchedule();
        Double consultationFee = masterSchedule.getConsultationFee();

        // 4. Generate unique appointment number
        String appointmentNumber = "APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 5. Build and save the Appointment
        Appointment appointment = Appointment.builder()
                .appointmentNumber(appointmentNumber)
                .hospital(timeSlot.getHospital())
                .timeSlot(timeSlot)
                .doctor(timeSlot.getDoctor())
                .patient(user)
                .appointmentDate(dailySession.getSessionDate())
                .paymentAmount(consultationFee)
                .status(AppointmentStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment [{}] booked successfully for patient [{}]",
                appointmentNumber, user.getEmail());

        // 6. Publish event for async email notification
        AppointmentBookedEvent event = AppointmentBookedEvent.builder()
                .appointmentNumber(appointmentNumber)
                .patientEmail(user.getEmail())
                .patientName(user.getFullName())
                .doctorName(timeSlot.getDoctor().getUser().getFullName())
                .hospitalName(timeSlot.getHospital().getName())
                .appointmentDate(dailySession.getSessionDate())
                .slotTime(timeSlot.getSlotTime())
                .consultationFee(consultationFee)
                .build();

        eventPublisher.publishEvent(event);

        // 7. Build and return response DTO
        return AppointmentResponseDto.builder()
                .appointmentNumber(savedAppointment.getAppointmentNumber())
                .doctorName(timeSlot.getDoctor().getUser().getFullName())
                .hospitalName(timeSlot.getHospital().getName())
                .appointmentDate(savedAppointment.getAppointmentDate())
                .slotTime(timeSlot.getSlotTime())
                .consultationFee(consultationFee)
                .status(savedAppointment.getStatus())
                .paymentStatus(savedAppointment.getPaymentStatus())
                .createdAt(savedAppointment.getCreatedAt())
                .build();
    }
}
