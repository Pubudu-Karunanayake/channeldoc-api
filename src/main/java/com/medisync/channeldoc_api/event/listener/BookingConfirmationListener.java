package com.medisync.channeldoc_api.event.listener;

import com.medisync.channeldoc_api.event.AppointmentBookedEvent;
import com.medisync.channeldoc_api.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listens for {@link AppointmentBookedEvent} and triggers email notifications.
 *
 * <p>{@code @TransactionalEventListener(AFTER_COMMIT)} ensures this only fires
 * after the booking transaction successfully commits — preventing phantom emails
 * on rollback (e.g., optimistic lock failure).</p>
 *
 * <p>{@code @Async("emailTaskExecutor")} pushes execution to the custom
 * managed thread pool, ensuring the HTTP response returns immediately.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingConfirmationListener {

    private final EmailNotificationService emailNotificationService;

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        log.info("Async event received for appointment [{}] on thread [{}]",
                event.getAppointmentNumber(), Thread.currentThread().getName());

        emailNotificationService.sendBookingConfirmation(event);
    }
}
