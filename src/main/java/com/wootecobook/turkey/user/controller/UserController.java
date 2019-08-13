package com.wootecobook.turkey.user.controller;

import com.wootecobook.turkey.user.service.UserService;
import com.wootecobook.turkey.user.service.dto.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public RedirectView create(UserRequest userRequest) {
        userService.save(userRequest);
        return new RedirectView("/");
    }
}
