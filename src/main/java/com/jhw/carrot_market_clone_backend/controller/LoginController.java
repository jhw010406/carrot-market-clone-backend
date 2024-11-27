package com.jhw.carrot_market_clone_backend.controller;

import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.service.JwtService;
import com.jhw.carrot_market_clone_backend.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private final LoginService loginService;

    LoginController(
            LoginService loginService
    ) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserInformation> login(
            @RequestBody UserCertificate inputUserCertificate
    ) {
        HttpHeaders headers = new HttpHeaders();
        String  userId = inputUserCertificate.getId();
        UserInformation userInformation = null;

        try {
            userInformation = loginService.login(inputUserCertificate);

            headers.set("Authorization", "Bearer " + JwtService.generateAccessToken(userId));
            headers.add("Refresh-Token", "Bearer " + JwtService.generateRefreshToken(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .headers(headers)
                .body(userInformation);
    }
}
