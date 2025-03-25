package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.JwtResponse;
import com.mycompany.flightapp.dto.LoginRequest;
import com.mycompany.flightapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {

        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//      Extract the role from the UserDetails object
        String role = userDetails.getAuthorities().stream()
                .findFirst() // Assuming there is only one role
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")) // Remove the ROLE_ prefix
                .orElseThrow(() -> new RuntimeException("Role not found"));
        String token = this.jwtUtil.generateJwtToken(userDetails.getUsername(), role);
        Date date = this.jwtUtil.getExpirationDateFromJwtToken(token);
        JwtResponse response = JwtResponse.builder()
                .token(token).expiryDate(date).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
