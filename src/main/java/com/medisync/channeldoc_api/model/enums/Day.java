package com.medisync.channeldoc_api.model.enums;

import java.time.DayOfWeek;

public enum Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    /**
     * Converts this Day enum to the corresponding {@link java.time.DayOfWeek}.
     * Used for date arithmetic with {@link java.time.temporal.TemporalAdjusters}.
     */
    public DayOfWeek toDayOfWeek() {
        return DayOfWeek.valueOf(this.name());
    }
}

