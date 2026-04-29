package com.medisync.channeldoc_api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.medisync.channeldoc_api.dto.request.GoogleAuthRequestDto;
import com.medisync.channeldoc_api.dto.response.AuthResponseDto;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.security.JwtService;
import com.medisync.channeldoc_api.service.AuthService;
import com.medisync.channeldoc_api.service.GoogleTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDto authenticateWithGoogle(GoogleAuthRequestDto request) {
        // 1. Verify Google ID token (signature, audience, expiry, email_verified)
        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(request.getIdToken());
        // 2. Extract user info from verified token
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String fullName = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        // 3. Find or create user
        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, fullName, pictureUrl, googleId))
                .orElseGet(() -> createNewUser(googleId, email, fullName, pictureUrl));

        // 4. Generate application JWT
        String token = jwtService.generateToken(user);
        // 5. Build response
        return buildAuthResponse(token, user);
    }

    private User updateExistingUser(User user, String fullName, String pictureUrl, String googleId) {
        boolean updated = false;

        if (fullName != null && !fullName.equals(user.getFullName())) {
            user.setFullName(fullName);
            updated = true;
        }
        if (pictureUrl != null && !pictureUrl.equals(user.getProfileImageUrl())) {
            user.setProfileImageUrl(pictureUrl);
            updated = true;
        }
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            user.setAuthProvider(AuthProvider.GOOGLE);
            updated = true;
        }

        if (updated) {
            user = userRepository.save(user);
            log.info("Updated existing user: {}", user.getEmail());
        }

        return user;
    }

    private User createNewUser(String googleId, String email, String fullName, String pictureUrl) {
        User newUser = User.builder()
                .googleId(googleId)
                .email(email)
                .fullName(fullName)
                .profileImageUrl(pictureUrl)
                .authProvider(AuthProvider.GOOGLE)
                .roles(Set.of(UserRole.ROLE_PATIENT))
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("Created new user via Google Sign-In: {}", savedUser.getEmail());
        return savedUser;
    }

    private AuthResponseDto buildAuthResponse(String token, User user) {
        // Use the first role as the primary role for the response
        String primaryRole = user.getRoles().stream()
                .findFirst()
                .map(UserRole::name)
                .orElse(UserRole.ROLE_PATIENT.name());

        Long hospitalId = user.getHospital() != null ? user.getHospital().getId() : null;

        AuthResponseDto.UserInfo userInfo = AuthResponseDto.UserInfo.builder()
                .id(user.getId())
                .name(user.getFullName())
                .hospitalId(hospitalId)
                .role(primaryRole)
                .build();

        return AuthResponseDto.builder()
                .token(token)
                .user(userInfo)
                .build();
    }
}
