package com.jhw.carrot_market_clone_backend.service;

import com.jhw.carrot_market_clone_backend.model.exception.DataAlreadyExistException;
import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import com.jhw.carrot_market_clone_backend.repository.UserCertificateRepository;
import com.jhw.carrot_market_clone_backend.repository.UserInformationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {

    private final PasswordEncodeService passwordEncoder;

    private final UserCertificateRepository userCertificateRepository;
    private final UserInformationRepository userInformationRepository;

    public RegisterService(
            PasswordEncodeService passwordEncoder,
            UserInformationRepository userInformationRepository,
            UserCertificateRepository userCertificateRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userInformationRepository = userInformationRepository;
        this.userCertificateRepository = userCertificateRepository;
    }

    public UserInformation register(
            UserCertificate inputUserCertificate
    ) throws RuntimeException {
        Optional<UserCertificate> foundUserInfo = userCertificateRepository.findByUserId(inputUserCertificate.getId());
        String                      userId = inputUserCertificate.getId();
        String                      password = passwordEncoder.encodePassword(inputUserCertificate.getPassword());
        UserCertificate             userCertificate;
        UserInformation             userInfo;

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

            return userInfo;
        }

        // when the account is already exist
        throw new DataAlreadyExistException("Account is already exist");
    }
}
