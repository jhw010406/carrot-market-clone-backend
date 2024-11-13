package com.jhw.carrot_market_clone_backend.controller;


import com.jhw.carrot_market_clone_backend.model.exception.DataAlreadyExistException;
import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.repository.UserCertificateRepository;
import com.jhw.carrot_market_clone_backend.repository.UserInformationRepository;
import com.jhw.carrot_market_clone_backend.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RegisterController {
    private final UserCertificateRepository userCertificateRepository;
    private final UserInformationRepository userInformationRepository;

    public RegisterController(
            UserInformationRepository userInformationRepository,
            UserCertificateRepository userCertificateRepository
    ) {
        this.userInformationRepository = userInformationRepository;
        this.userCertificateRepository = userCertificateRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCertificate> register(@RequestBody UserCertificate inputUserCertificate) {
        Optional<UserCertificate>   foundUserInfo = userCertificateRepository.findByUserId(inputUserCertificate.getId());
        String                      userId = inputUserCertificate.getId();
        String                      password = inputUserCertificate.getPassword();
        UserCertificate             userCertificate;
        UserInformation             userInfo;
        HttpHeaders                 headers = new HttpHeaders();

        // If the account is not exist
        if (foundUserInfo.isEmpty()) {
            userCertificate = new UserCertificate(
                    null,
                    userId,
                    password
            );
            userCertificateRepository.save(userCertificate);

            userInfo = new UserInformation(
                    userCertificate.getUid(),
                    userId,
                    null
            );
            userInformationRepository.save(userInfo);

            headers.add("Authorization", "Bearer " + JwtService.generateAccessToken(userId));
            headers.add("Refresh-Token", "Bearer " + JwtService.generateRefreshToken(userId));

            return new ResponseEntity<>(userCertificate, headers, HttpStatus.OK);
        }

        // when the account is already exist
        throw new DataAlreadyExistException("Account is already exist");
    }
}
