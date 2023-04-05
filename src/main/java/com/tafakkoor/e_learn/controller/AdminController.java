package com.tafakkoor.e_learn.controller;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.enums.Levels;
import com.tafakkoor.e_learn.enums.Roles;
import com.tafakkoor.e_learn.services.UserService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public ModelAndView adminPage() {
        ModelAndView modelAndView = new ModelAndView();
        List<AuthUser> allUsers = userService.getAllUsers();
        modelAndView.addObject("users", allUsers);
        modelAndView.addObject("roles", Roles.values());
        modelAndView.setViewName("admin/adminpage");
        return modelAndView;
    }
    @PostMapping("/admin/updateStatus")
    public String updateStatus(@RequestParam("updated_id") Long id) {
        userService.updateStatus(id);
        return "redirect:/admin";
    }
    @PostMapping("/admin/updateRole")
    public String updateRole(@RequestParam("updated_id") Long id, @RequestParam("isAdmin") String role , @RequestParam("isTeacher") String role2) {
        if(role.equals("ADMIN")){
            userService.updateRole(id, role);
        }
        if(role2.equals("TEACHER")){
            userService.updateRole(id, role2);
        }
        return "redirect:/admin";
    }
}
