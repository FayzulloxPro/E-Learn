package com.tafakkoor.e_learn.config.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private LinkedIn linkedIn;

    @GetMapping("/profile")
    public String profile(Model model) {
        System.out.println("profile controller isAuthenticated() = " + isAuthenticated());
        if (!isAuthenticated()) {
            return "redirect:/connect/linkedin";
        }
        LinkedInProfileFull profile = linkedIn.profileOperations().getUserProfileFull();
        model.addAttribute("profile", profile);
        System.out.println("profile = " + profile);
        return "home";
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}

