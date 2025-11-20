package com.proofy.proofy_api.global;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.ParseError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.error.ProofParseException;
import com.proofy.proofy_api.checker.error.CheckError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("[ServerError]", ex);

        ErrorResponse response = ErrorResponse.builder()
                .error("SERVER_ERROR")
                .message("Unexpected server error occurred")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}