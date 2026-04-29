package com.medisync.channeldoc_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleTokenVerifier {

    /**
     * Verifies the Google ID token and returns the payload.
     * Performs signature verification, audience check, issuer check, and expiry check.
     *
     * @param idTokenString the raw ID token string from the frontend
     * @return the verified token payload containing user info
     * @throws com.medisync.channeldoc_api.exception.InvalidTokenException if the token is invalid
     */
    GoogleIdToken.Payload verifyToken(String idTokenString);
}
