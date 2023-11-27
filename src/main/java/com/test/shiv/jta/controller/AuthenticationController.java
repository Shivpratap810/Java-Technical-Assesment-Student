package com.test.shiv.jta.controller;

import com.test.shiv.jta.config.JWTAuth.AuthenticationRequest;
import com.test.shiv.jta.config.JWTAuth.AuthenticationResponse;
import com.test.shiv.jta.config.JWTAuth.JwtUtils;
import com.test.shiv.jta.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtils.generateJwtToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
