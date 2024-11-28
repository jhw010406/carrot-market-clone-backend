package com.jhw.carrot_market_clone_backend.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncodeService {

    public PasswordEncodeService() {}

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matchPassword(String password, String hashedPassword) {
        // 해시된 비밀번호와 동일한 salt 값으로 password를 hash한 후, hashedPassword와 비교함.
        return BCrypt.checkpw(password, hashedPassword);
    }
}
