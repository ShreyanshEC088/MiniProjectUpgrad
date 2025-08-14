package org.zeta.resturant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zeta.resturant.dao.UserRepository;
import org.zeta.resturant.model.User;
import org.zeta.resturant.service.MyUserDetailsService;
import org.zeta.resturant.util.JwtUtil;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("plain");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = authController.register(user);

        assertEquals("test", result.getUsername());
        assertEquals("encoded", result.getPassword());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        Map<String, String> req = Map.of("username", "test", "password", "plain");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encoded");
        when(passwordEncoder.matches("plain", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

        Map<String, String> result = authController.login(req);

        assertEquals(Collections.singletonMap("jwt", "jwt-token"), result);
    }

    @Test
    void testLoginFailure() {
        Map<String, String> req = Map.of("username", "test", "password", "wrong");
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encoded");
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authController.login(req));
    }
}
