package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.security.commands.LoginCredentials;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping(value = "login")
    public void login(@RequestBody LoginCredentials credentials){
        System.out.println("test");
    }
}
