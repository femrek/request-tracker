package me.femrek.viewcounter.advice;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.error.AppSubscriptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice(assignableTypes = {
        me.femrek.viewcounter.controller.SubscriptionRestController.class
})
@Log4j2
public class SubscriptionControllerAdvice {
    @ExceptionHandler(AppSubscriptionNotFound.class)
    public ResponseEntity<String> handleAppSubscriptionNotFound(AppSubscriptionNotFound ex) {
        log.debug("AppSubscriptionNotFound: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.debug("IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid argument: " + ex.getName() +
                " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        log.debug("MethodArgumentTypeMismatchException: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
