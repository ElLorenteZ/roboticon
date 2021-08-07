package io.lorentez.roboticon.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@ControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolation(ConstraintViolationException exception,
                                          ServletWebRequest request) throws IOException {
        request.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
    }

    
}
