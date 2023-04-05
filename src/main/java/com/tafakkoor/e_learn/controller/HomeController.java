package com.tafakkoor.e_learn.controller;

import com.tafakkoor.e_learn.exceptions.CustomRuntimeException;
import com.tafakkoor.e_learn.config.security.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Random;

@Controller
public class HomeController {

    private final UserSession userSession;

    public HomeController(UserSession userSession) {
        this.userSession = userSession;
    }

    @GetMapping("/home")
    public String hasAdminRole(Model model, Principal principal) {
        return "home";
    }

    @GetMapping("/home2")
    /*@RolesAllowed("ADMIN")*/
    public String homePage2() {
        return "main2";
    }
}