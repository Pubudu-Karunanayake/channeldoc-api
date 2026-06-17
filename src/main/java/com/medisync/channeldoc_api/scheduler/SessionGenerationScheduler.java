package com.medisync.channeldoc_api.scheduler;

import com.medisync.channeldoc_api.service.SessionGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Scheduled task that maintains the 14-day rolling availability window.
 * Runs daily at midnight and generates DailySession + TimeSlot records
 * for the date that is exactly 14 days in the future.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionGenerationScheduler {

    private static final int ROLLING_WINDOW_DAYS = 14;

    private final SessionGenerationService sessionGenerationService;

    /**
     * Executes every day at midnight (00:00:00).
     * Calculates the target date (today + 14 days) and delegates
     * session generation to the SessionGenerationService.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailySessions() {
        LocalDate targetDate = LocalDate.now().plusDays(ROLLING_WINDOW_DAYS);
        log.info("Midnight scheduler: generating sessions for target date {} (today + {} days)",
                targetDate, ROLLING_WINDOW_DAYS);

        try {
            sessionGenerationService.generateSessionsForDate(targetDate);
        } catch (Exception e) {
            log.error("Failed to generate sessions for date {}: {}", targetDate, e.getMessage(), e);
        }
    }
}
