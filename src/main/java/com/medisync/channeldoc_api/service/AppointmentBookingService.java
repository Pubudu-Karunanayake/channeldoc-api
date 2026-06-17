package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.AppointmentBookingRequestDto;
import com.medisync.channeldoc_api.dto.response.AppointmentResponseDto;
import com.medisync.channeldoc_api.model.User;

public interface AppointmentBookingService {
    AppointmentResponseDto bookAppointment(AppointmentBookingRequestDto request, User user);
}
