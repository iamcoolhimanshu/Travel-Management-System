package com.codeWithHimanshu.Travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping({"/", "/index"})
    public String home() {
        return "redirect:/travels";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
