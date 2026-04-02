package com.digitaltherapyassistant.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    public TokenBlackListService() {}

    public void blacklist(String token){
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token){
        return blacklistedTokens.contains(token);
    }
}
