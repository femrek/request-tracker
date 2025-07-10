package me.femrek.viewcounter.advice;

import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.error.AppSubscriptionNotFound;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        me.femrek.viewcounter.controller.SubscriptionController.class,
        me.femrek.viewcounter.controller.MainController.class
})
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(AppSubscriptionNotFound.class)
    public String handleAppSubscriptionNotFound(AppSubscriptionNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("An unexpected error occurred: ", ex);
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error-page";
    }
}
