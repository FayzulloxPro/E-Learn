package com.tafakkoor.e_learn.handlers;

import com.tafakkoor.e_learn.exceptions.CustomRuntimeException;
import com.tafakkoor.e_learn.exceptions.PermissionDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice("com.tafakkoor.e_learn")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Model model, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/500");
        return mav;
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ModelAndView exception1() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/403");
        return mav;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView exception2() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/401");
        return mav;
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ModelAndView exception3() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/403");
        return mav;
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ModelAndView exception4() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errors/404");
        return mav;
    }
}
