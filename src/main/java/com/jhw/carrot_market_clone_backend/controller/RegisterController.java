package com.jhw.carrot_market_clone_backend.controller;


import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.service.JwtService;
import com.jhw.carrot_market_clone_backend.service.RegisterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(
            RegisterService registerService
    ) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserInformation> register(
            @RequestBody UserCertificate inputUserCertificate
    ) {
        HttpHeaders     headers = new HttpHeaders();
        String          userId = inputUserCertificate.getId();
        UserInformation userInformation = null;

        try {
            userInformation = registerService.register(inputUserCertificate);

            headers.set("Authorization", "Bearer " + JwtService.generateAccessToken(userId));
            headers.add("Refresh-Token", "Bearer " + JwtService.generateRefreshToken(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(userInformation);
    }
}
