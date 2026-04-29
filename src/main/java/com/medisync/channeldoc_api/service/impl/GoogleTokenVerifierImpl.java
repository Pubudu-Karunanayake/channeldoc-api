package com.medisync.channeldoc_api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.medisync.channeldoc_api.exception.InvalidTokenException;
import com.medisync.channeldoc_api.service.GoogleTokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerifierImpl implements GoogleTokenVerifier {

    private final String clientId;

    public GoogleTokenVerifierImpl(@Value("${google.client-id}") String clientId) {
        this.clientId = clientId;
    }

    /**
     * Verifies the Google ID token and returns the payload.
     * Performs signature verification, audience check, issuer check, and expiry check.
     *
     * @param idTokenString the raw ID token string from the frontend
     * @return the verified token payload containing user info
     * @throws InvalidTokenException if the token is invalid, expired, or tampered with
     */
    @Override
    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        System.out.println("1");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();

                System.out.println("2");
        GoogleIdToken idToken;
        System.out.println("3");            
        try {
            System.out.println("4");
            idToken = verifier.verify(idTokenString);
            System.out.println("5");    
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("6");
            throw new InvalidTokenException("Failed to verify Google ID token", e);
        }

        if (idToken == null) {
            System.out.println("7");
            throw new InvalidTokenException("Invalid Google ID token: verification failed");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        // Ensure email is verified by Google
        Boolean emailVerified = payload.getEmailVerified();
        if (emailVerified == null || !emailVerified) {
            System.out.println("8");
            throw new InvalidTokenException("Google account email is not verified");
        }

        System.out.println("9");
        return payload;
    }
}
