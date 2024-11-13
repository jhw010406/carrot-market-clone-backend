package com.jhw.carrot_market_clone_backend.controller;

import com.jhw.carrot_market_clone_backend.model.exception.ServerErrorException;
import com.jhw.carrot_market_clone_backend.model.exception.UnauthorizedException;
import com.jhw.carrot_market_clone_backend.model.exception.UserNotFoundException;
import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.repository.UserCertificateRepository;
import com.jhw.carrot_market_clone_backend.repository.UserInformationRepository;
import com.jhw.carrot_market_clone_backend.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LoginController {
    private final UserCertificateRepository userCertificateRepository;
    private final UserInformationRepository userInformationRepository;

    LoginController(
            UserCertificateRepository userCertificateRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.userCertificateRepository = userCertificateRepository;
        this.userInformationRepository = userInformationRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<UserInformation> login(@RequestBody UserCertificate inputUserCertificate) {
        Optional<UserCertificate> userCertificate;
        Optional<UserInformation> userInfo;
        HttpHeaders headers = new HttpHeaders();
        String  userId = inputUserCertificate.getId();
        Integer userUid;
        String  userPassword;
        String  userAccessToken;
        String  userRefreshToken;

        userCertificate = userCertificateRepository.findByUserId(userId);

        // account is not exist
        if (userCertificate.isEmpty()) {
            // 로그인 대상 유저가 없으므로 예외 발생
            throw new UserNotFoundException("ID : " + inputUserCertificate.getId());
        }
        else{
            userUid = userCertificate.get().getUid();
            userPassword = userCertificate.get().getPassword();

            // input password equals to account's password
            if (userPassword.equals(inputUserCertificate.getPassword())) {
                userInfo = userInformationRepository.findById(userUid.toString());

                // server error, need retry
                if (userInfo.isEmpty()) {
                    throw new ServerErrorException("Can't find user's information");
                }
                else{
                    userAccessToken = JwtService.generateAccessToken(userId);
                    userRefreshToken = JwtService.generateRefreshToken(userId);

                    headers.set("Authorization", "Bearer " + userAccessToken);
                    headers.add("Refresh-Token", "Bearer " + userRefreshToken);

                    return  new ResponseEntity<>(userInfo.get(), headers, HttpStatus.ACCEPTED);
                }
            }

            // input password different with account's password
            throw new UnauthorizedException("Login failed");
        }
    }
}
