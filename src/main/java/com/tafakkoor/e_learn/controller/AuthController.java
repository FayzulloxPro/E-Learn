package com.tafakkoor.e_learn.controller;

import  com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.domain.Token;
import com.tafakkoor.e_learn.dto.UserRegisterDTO;
import com.tafakkoor.e_learn.enums.Status;
import com.tafakkoor.e_learn.repository.AuthUserRepository;
import com.tafakkoor.e_learn.repository.TokenRepository;
import com.tafakkoor.e_learn.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping( "/auth" )
public class AuthController {
    private final AuthUserRepository authUserRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    public AuthController(AuthUserRepository authUserRepository, UserService userService, TokenRepository tokenRepository ) {
        this.authUserRepository = authUserRepository;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping( "/register" )
    public String registerPage(Model model) {
        model.addAttribute("dto", new UserRegisterDTO());
        return "auth/register";
    }

    @GetMapping( "/login" )
    public ModelAndView loginPage( @RequestParam( required = false ) String error ) {
        var mav = new ModelAndView();
        mav.addObject("error", error);
        mav.setViewName("auth/login");
        return mav;
    }


    @GetMapping( "/logout" )
    public ModelAndView logoutPage() {
        var mav = new ModelAndView();
        mav.setViewName("auth/logout");
        return mav;
    }

    @PostMapping( "/register" )
    public String register( @Valid @ModelAttribute UserRegisterDTO dto, BindingResult result ) {
        if ( result.hasErrors() ) {
            return "auth/register";
        }

        if ( !dto.getPassword().equals(dto.getConfirmPassword()) ) {
            result.rejectValue("confirmPassword", "", "Passwords do not match");
            return "auth/register";
        }

        userService.saveUserAndSendEmail(dto);

        return "auth/verify_email";
    }

    @GetMapping( "/activate" )
    public String activate( @RequestParam( name = "token" ) String token ) {
        Optional<Token> byToken = tokenRepository.findByToken(token);
        if(byToken.isPresent()){
            Token token1 = byToken.get();
            if ( !token1.getValidTill().isBefore(LocalDateTime.now()) ) {
                AuthUser user = token1.getUser();
                user.setStatus(Status.ACTIVE);
                authUserRepository.save(user);
                return "auth/code_activated";
            } else {
                return "auth/code_expired";
            }
        }else{
            return "auth/code_expired";
        }
    }



}
