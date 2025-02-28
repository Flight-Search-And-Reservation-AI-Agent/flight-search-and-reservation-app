//package com.mycompany.flightapp.controller;
//
//import com.mycompany.flightapp.dto.JwtResponse;
//import com.mycompany.flightapp.dto.LoginRequest;
//import com.mycompany.flightapp.service.CustomUserDetailsService;
//import com.mycompany.flightapp.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthenticationController {
//    private AuthenticationManager authenticationManager;
//
//    private JwtUtil jwtUtil;
//
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
//        try {
//            // Authenticate the user
//            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            //Load user details and generate token
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
//            String jwt = jwtUtil.generateJwtToken(userDetails.getUsername());
//            Date expiryDate = jwtUtil.getExpirationDateFromJwtToken(jwt);
//
//            // Return JWT response with token and expiry date
//            return ResponseEntity.ok(new JwtResponse(jwt, expiryDate));
//        }
//        catch (AuthenticationException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//    }
//}
