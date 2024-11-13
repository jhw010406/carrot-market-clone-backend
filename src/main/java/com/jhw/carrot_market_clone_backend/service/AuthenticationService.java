package com.jhw.carrot_market_clone_backend.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthenticationService {

    public Boolean isValidAccessToken(String token, String inputUserId) {
        String  userIdInToken =JwtService.getUserIdInToken(token);
        long    tokenExpireTime;

        if (inputUserId.equals(userIdInToken)) {
            tokenExpireTime = JwtService.getExpireTimeInToken(token);

            if (tokenExpireTime < Instant.now().getEpochSecond()) {
                return true;
            }
            System.out.println("[ isValidToken ] access token expired");
        }
        else {
            System.out.println("[ isValidToken ] access token not valid");
        }

        return false;
    }

    public Boolean isValidRefreshToken(String token, String inputUserId) {
        String  userIdInToken =JwtService.getUserIdInToken(token);
        long    tokenExpireTime;

        if (inputUserId.equals(userIdInToken)) {
            tokenExpireTime = JwtService.getExpireTimeInToken(token);

            if (tokenExpireTime < Instant.now().getEpochSecond()) {
                return true;
            }
            System.out.println("[ isValidRefreshToken ] refresh token expired");
        }
        else {
            System.out.println("[ isValidRefreshToken ] refresh token not valid");
        }

        return false;
    }
}