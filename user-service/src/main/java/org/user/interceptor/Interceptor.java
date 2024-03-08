package org.user.interceptor;

import centwong.utility.exception.ForbiddenException;
import centwong.utility.response.HttpResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Interceptor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> handleException(MethodArgumentNotValidException ex){
        var messages = ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

        return ResponseEntity
                .internalServerError()
                .body(
                        HttpResponse.sendErrorResponse(messages.toString(), false)
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpResponse> handleException(RuntimeException ex){
        return ResponseEntity
                .internalServerError()
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), false)
                );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<HttpResponse> handleException(ForbiddenException ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        HttpResponse.sendErrorResponse(ex.getMessage(), false)
                );
    }
}
