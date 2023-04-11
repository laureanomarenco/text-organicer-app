package com.textorganicer.excepciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class ConfigExceptionHandler {
    /* 500 */
    @ExceptionHandler(ErrorProcessException.class)
    public ResponseEntity<?> handleEnteredDataConflict(HttpServletRequest request,
                                                       ErrorProcessException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", Boolean.FALSE,
                        "mensaje", e.getMessage()));
    }

    // Not Found Exception
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleDataNotFound(
            HttpServletRequest request,
            NotFoundException e
    ) {
        Map<String, Object> res = new HashMap<>();

        res.put("success", Boolean.FALSE);
        res.put("status", HttpStatus.BAD_REQUEST);
        res.put("mensaje", e.getMessage());
        return ResponseEntity.badRequest()
                .body(res);
    }



    // Auth Exception
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthError(
            HttpServletRequest request,
            AuthException a
    ) {
        Map<String, Object> res = new HashMap<>();

        res.put("status", HttpStatus.UNAUTHORIZED);
        res.put("success", Boolean.FALSE);
        res.put("mensaje", a.getMessage());

        log.error("AuthError - ", a.getMessage());
        return ResponseEntity.badRequest()
                .body(res);
    }

    // Validators Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> argumentNotValidationRequest(HttpServletRequest request, MethodArgumentNotValidException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "success", Boolean.FALSE,
                        "mensaje", e.getFieldError().getDefaultMessage()));
    }
}
