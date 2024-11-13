package com.jhw.carrot_market_clone_backend.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 반환 상태 코드 설정
@ResponseStatus(HttpStatus.NOT_FOUND)
// 런타임 예외 발생
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
