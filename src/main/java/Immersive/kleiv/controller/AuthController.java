package Immersive.kleiv.controller;

import Immersive.kleiv.Entity.User;
import Immersive.kleiv.Services.CustomUserDetailsService;
import Immersive.kleiv.configurations.JwtService;
import Immersive.kleiv.dtos.AutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${app.baserUrl}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationProvider authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userDetailsService.registerUser(user);
    }
    @PostMapping("/activation")
    public void activate(@RequestBody Map<String , String> activationCodes) {
        userDetailsService.activateUser(activationCodes);
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AutDto autDto) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(autDto.username(), autDto.password()));
            if (auth.isAuthenticated()) {
                System.out.println("autDto = " + autDto);
                return jwtService.generateToken(autDto.username());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
