package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.GoogleAuthRequestDto;
import com.medisync.channeldoc_api.dto.response.AuthResponseDto;

public interface AuthService {

    /**
     * Authenticates a user using a Google ID token.
     * Verifies the token, finds or creates the user, and returns an application JWT.
     *
     * @param request containing the Google ID token
     * @return AuthResponse with application JWT and user info
     */
    AuthResponseDto authenticateWithGoogle(GoogleAuthRequestDto request);
}
