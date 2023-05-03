package com.backend.management.controller;

import com.backend.management.model.Token;
import com.backend.management.model.User;
import com.backend.management.model.UserRole;
import com.backend.management.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate/tenant")
    public Token authenticateTenant(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_TENANT);
    }

    @PostMapping("/authenticate/manager")
    public Token authenticateManager(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_MANAGER);
    }

    @PostMapping("/authenticate/provider")
    public Token authenticateProvider(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_PROVIDER);
    }

}
