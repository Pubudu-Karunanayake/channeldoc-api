package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.event.AppointmentBookedEvent;

public interface EmailNotificationService {
    void sendBookingConfirmation(AppointmentBookedEvent event);
}
