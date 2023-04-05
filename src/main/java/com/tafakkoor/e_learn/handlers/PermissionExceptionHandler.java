package com.tafakkoor.e_learn.handlers;

import com.tafakkoor.e_learn.exceptions.PermissionDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class PermissionExceptionHandler {
    @ExceptionHandler(PermissionDeniedException.class)
    public ModelAndView exception() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/403");
        return mav;
    }
}
