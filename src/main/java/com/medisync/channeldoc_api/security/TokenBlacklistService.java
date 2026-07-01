package com.medisync.channeldoc_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    /**
     * Adds a token to the blacklist in Redis with the specified time-to-live.
     *
     * @param token The JWT token to blacklist
     * @param timeToLiveMillis The duration for which the token should be blacklisted (typically its remaining validity)
     */
    public void blacklistToken(String token, long timeToLiveMillis) {
        if (timeToLiveMillis > 0) {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(key, "blacklisted", timeToLiveMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Checks if a token is present in the blacklist.
     *
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
