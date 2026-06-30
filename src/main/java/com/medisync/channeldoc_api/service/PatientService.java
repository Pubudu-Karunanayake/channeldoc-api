package com.medisync.channeldoc_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.medisync.channeldoc_api.dto.request.PatientRegistrationRequestDto;
import com.medisync.channeldoc_api.model.User;

public interface PatientService {

    /**
     * Registers a new patient via Google Sign-Up.
     * Validates email uniqueness, creates and persists the User entity
     * with ROLE_PATIENT, and creates the associated PatientProfile.
     *
     * @param payload the verified Google ID token payload
     * @param request the registration request containing patient profile details
     * @return the saved User entity
     */
    User registerPatientViaGoogle(GoogleIdToken.Payload payload, PatientRegistrationRequestDto request);

    /**
     * Retrieves the paginated appointment history for the logged-in patient.
     */
    com.medisync.channeldoc_api.dto.response.RestPage<com.medisync.channeldoc_api.dto.response.PatientAppointmentHistoryResponseDto> getMyAppointmentHistory(User user, org.springframework.data.domain.Pageable pageable);
}

