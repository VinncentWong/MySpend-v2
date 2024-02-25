package org.example.interceptor;

import centwong.utility.response.HttpResponse;
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
                .map(ObjectError::toString)
                .toList();
        var totalMessage = "";
        for(var m: messages){
            totalMessage += m + "\n";
        }

        return ResponseEntity
                .internalServerError()
                .body(
                        HttpResponse.sendErrorResponse(totalMessage, false)
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
}
