package org.zeta.resturant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeta.resturant.dao.UserRepository;
import org.zeta.resturant.model.User;
import org.zeta.resturant.service.MyUserDetailsService;
import org.zeta.resturant.util.JwtUtil;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        logger.info("Registering user: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        logger.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        logger.info("Login attempt for user: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(req.get("password"), userDetails.getPassword())) {
            String token = jwtUtil.generateToken(userDetails);
            logger.info("Login successful for user: {}", username);
            return Collections.singletonMap("jwt", token);
        } else {
            logger.warn("Login failed for user: {}", username);
            throw new RuntimeException("Invalid credentials");
        }
    }
}