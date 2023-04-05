package com.textorganicer.excepciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class ConfigExceptionHandler {

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
        res.put("message", a.getMessage());

        log.error("AuthError - ", a.getMessage());
        return ResponseEntity.badRequest()
                .body(res);
    }
}
