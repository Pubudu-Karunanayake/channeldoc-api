package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.model.DailySession;
import com.medisync.channeldoc_api.model.MasterSchedule;
import com.medisync.channeldoc_api.model.TimeSlot;
import com.medisync.channeldoc_api.model.enums.Day;
import com.medisync.channeldoc_api.model.enums.SessionStatus;
import com.medisync.channeldoc_api.repository.DailySessionRepository;
import com.medisync.channeldoc_api.repository.MasterScheduleRepository;
import com.medisync.channeldoc_api.repository.TimeSlotRepository;
import com.medisync.channeldoc_api.service.SessionGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionGenerationServiceImpl implements SessionGenerationService {

    private final MasterScheduleRepository masterScheduleRepository;
    private final DailySessionRepository dailySessionRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public void generateSessionsForSchedule(MasterSchedule schedule, int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);
        DayOfWeek targetDayOfWeek = schedule.getDay().toDayOfWeek();

        // Find the first occurrence of this day-of-week on or after today
        LocalDate nextOccurrence = today.with(TemporalAdjusters.nextOrSame(targetDayOfWeek));

        int sessionsCreated = 0;
        while (!nextOccurrence.isAfter(endDate)) {
            if (createSessionWithTimeSlots(schedule, nextOccurrence)) {
                sessionsCreated++;
            }
            nextOccurrence = nextOccurrence.plusWeeks(1);
        }

        log.info("Generated {} session(s) for MasterSchedule [id={}, day={}, doctor={}] within next {} days",
                sessionsCreated, schedule.getId(), schedule.getDay(),
                schedule.getDoctor().getId(), days);
    }

    @Override
    @Transactional
    public void generateSessionsForDate(LocalDate targetDate) {
        // Determine which Day enum corresponds to this date
        DayOfWeek dayOfWeek = targetDate.getDayOfWeek();
        Day day = Day.valueOf(dayOfWeek.name());

        // Find all active master schedules for this day
        List<MasterSchedule> schedules = masterScheduleRepository.findByDayAndIsActiveTrue(day);

        if (schedules.isEmpty()) {
            log.debug("No active schedules found for {} ({})", targetDate, day);
            return;
        }

        int sessionsCreated = 0;
        for (MasterSchedule schedule : schedules) {
            if (createSessionWithTimeSlots(schedule, targetDate)) {
                sessionsCreated++;
            }
        }

        log.info("Daily scheduler: generated {} session(s) for {} ({}) from {} active schedule(s)",
                sessionsCreated, targetDate, day, schedules.size());
    }

    /**
     * Creates a single DailySession and its associated TimeSlot records
     * for the given MasterSchedule on the given date.
     *
     * @param schedule the MasterSchedule template
     * @param date     the concrete date for this session
     * @return true if a new session was created, false if it already existed
     */
    private boolean createSessionWithTimeSlots(MasterSchedule schedule, LocalDate date) {
        // Duplicate guard: skip if this session already exists
        if (dailySessionRepository.existsByMasterScheduleAndSessionDate(schedule, date)) {
            log.debug("Session already exists for MasterSchedule [id={}] on {}, skipping",
                    schedule.getId(), date);
            return false;
        }

        // 1. Create and save the DailySession
        DailySession session = DailySession.builder()
                .hospital(schedule.getHospital())
                .doctor(schedule.getDoctor())
                .masterSchedule(schedule)
                .sessionDate(date)
                .status(SessionStatus.ACTIVE)
                .build();

        DailySession savedSession = dailySessionRepository.save(session);

        // 2. Generate TimeSlot records from startTime to endTime
        List<TimeSlot> timeSlots = generateTimeSlots(schedule, savedSession);
        timeSlotRepository.saveAll(timeSlots);

        log.debug("Created session [id={}] with {} time slot(s) for date {}",
                savedSession.getId(), timeSlots.size(), date);

        return true;
    }

    /**
     * Generates a list of TimeSlot records by iterating from the schedule's
     * startTime to endTime in timePerPatient-minute increments.
     * Each slot is initialized as unbooked.
     */
    private List<TimeSlot> generateTimeSlots(MasterSchedule schedule, DailySession session) {
        List<TimeSlot> slots = new ArrayList<>();
        LocalTime currentTime = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();
        int intervalMinutes = schedule.getTimePerPatient();

        while (currentTime.isBefore(endTime)) {
            TimeSlot slot = TimeSlot.builder()
                    .hospital(schedule.getHospital())
                    .doctor(schedule.getDoctor())
                    .dailySession(session)
                    .slotTime(currentTime)
                    .isBooked(false)
                    .build();

            slots.add(slot);
            currentTime = currentTime.plusMinutes(intervalMinutes);
        }

        return slots;
    }
}
