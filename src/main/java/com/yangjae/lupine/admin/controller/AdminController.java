package com.yangjae.lupine.admin.controller;

import com.yangjae.lupine.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/login")
    public ModelAndView navigateToAdminLogin() {
        return new ModelAndView("admin/login");
    }

    @GetMapping("/home")
    public ModelAndView navigateToAdminHome() {
        return new ModelAndView("admin/home");
    }
}
