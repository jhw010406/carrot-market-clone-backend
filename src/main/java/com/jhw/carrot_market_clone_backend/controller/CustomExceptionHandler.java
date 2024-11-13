package com.jhw.carrot_market_clone_backend.controller;


import com.jhw.carrot_market_clone_backend.model.error.ErrorDetails;
import com.jhw.carrot_market_clone_backend.model.exception.DataAlreadyExistException;
import com.jhw.carrot_market_clone_backend.model.exception.ServerErrorException;
import com.jhw.carrot_market_clone_backend.model.exception.UnauthorizedException;
import com.jhw.carrot_market_clone_backend.model.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

// controlleradvice :   controller와 handler에서 발생하는 에러들을 잡는다
//                      exceptionhandler들을 bean으로 등록한다
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // exceptionhandler를 통해 usernotfoundexception 예외가 발생할 경우, handleUserNotFoundException 함수가 수행되도록 한다
    // usernotfoundexception 예외가 발생하면, spring framework에서 Exception ex, WebRequest request을 매개변수로써 아래 함수에 전달한다.
    @ExceptionHandler(UserNotFoundException.class)
    // responseentity<T> :  http 요청에 대한 응답 데이터(httpstatus, httpheaders, httpbody)를 포함하는 클래스
    //                      주어지는 자료형(T)이 httpbody를 맡는다
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        headers.set("connection", "close");

        return  new ResponseEntity<ErrorDetails>(errorDetails, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerErrorException.class)
    public final ResponseEntity<ErrorDetails> handleServerErrorException(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        headers.set("connection", "close");

        return  new ResponseEntity<ErrorDetails>(errorDetails, headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ErrorDetails> handleUnauthorizedException(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        headers.set("connection", "close");

        return new ResponseEntity<ErrorDetails>(errorDetails, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public final ResponseEntity<ErrorDetails> handleDataAlreadyExistException(Exception ex, WebRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        headers.set("connection", "close");

        return new ResponseEntity<ErrorDetails>(errorDetails, headers, HttpStatus.BAD_REQUEST);
    }
}
