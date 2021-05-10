package com.example.StudentProfile.controllers;

import com.example.StudentProfile.config.jwt.JwtProvider;
import com.example.StudentProfile.dto.AuthRequest;
import com.example.StudentProfile.dto.AuthResponse;
import com.example.StudentProfile.dto.RegistrationRequest;
import com.example.StudentProfile.models.User;
import com.example.StudentProfile.services.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private static final Logger logger = LogManager.getLogger(AdminController.class.getSimpleName());

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public AuthResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws Exception {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        userService.registerUser(u);
        String token = jwtProvider.generateToken(u.getId().toString(), u.getLogin(), u.getRole().getName());
        logger.info("control token " + token);
        return new AuthResponse(token);

    }

    @PostMapping("/signin")
    public AuthResponse authenticate(@RequestBody AuthRequest request) {
        User userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getId().toString(), userEntity.getLogin(), userEntity.getRole().getName());
        return new AuthResponse(token);
    }
}
