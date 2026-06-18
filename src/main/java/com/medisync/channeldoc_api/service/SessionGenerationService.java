package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.model.MasterSchedule;

import java.time.LocalDate;

public interface SessionGenerationService {

    /**
     * Generates DailySession and TimeSlot records for a single MasterSchedule
     * for all matching days within the specified number of days from today.
     * Called when a new MasterSchedule is created.
     *
     * @param schedule the newly created MasterSchedule
     * @param days     the number of days into the future to generate sessions for
     */
    void generateSessionsForSchedule(MasterSchedule schedule, int days);

    /**
     * Generates DailySession and TimeSlot records for ALL active MasterSchedules
     * that match the given target date's day of the week.
     * Called by the daily midnight scheduler.
     *
     * @param targetDate the specific future date to generate sessions for
     */
    void generateSessionsForDate(LocalDate targetDate);
}
