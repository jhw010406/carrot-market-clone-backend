package com.jhw.carrot_market_clone_backend.service;

import com.jhw.carrot_market_clone_backend.model.exception.ServerErrorException;
import com.jhw.carrot_market_clone_backend.model.exception.UnauthorizedException;
import com.jhw.carrot_market_clone_backend.model.exception.UserNotFoundException;
import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.repository.UserCertificateRepository;
import com.jhw.carrot_market_clone_backend.repository.UserInformationRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final PasswordEncodeService passwordEncoder;

    private final UserCertificateRepository userCertificateRepository;
    private final UserInformationRepository userInformationRepository;

    public LoginService(
            PasswordEncodeService passwordEncoder,
            UserCertificateRepository userCertificateRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userCertificateRepository = userCertificateRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public UserInformation login(
            UserCertificate inputUserCertificate
    ) throws RuntimeException {
        Optional<UserCertificate> userCertificate;
        Optional<UserInformation> userInfo;
        Integer userUid;
        String  userPassword;

        userCertificate = userCertificateRepository.findByUserId(inputUserCertificate.getId());

        // account is not exist
        if (userCertificate.isEmpty()) {
            // 로그인 대상 유저가 없으므로 예외 발생
            throw new UserNotFoundException("ID : " + inputUserCertificate.getId());
        }
        else{
            userUid = userCertificate.get().getUid();
            userPassword = userCertificate.get().getPassword();

            // input password equals to account's password
            if (passwordEncoder.matchPassword(inputUserCertificate.getPassword(), userPassword)) {
                userInfo = userInformationRepository.findById(userUid.toString());

                // server error, need retry
                if (userInfo.isEmpty()) {
                    throw new ServerErrorException("Can't find user's information");
                }
                else{
                    return  userInfo.get();
                }
            }

            // input password different with account's password
            throw new UnauthorizedException("Login failed");
        }
    }
}
