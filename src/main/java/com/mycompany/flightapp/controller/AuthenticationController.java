package com.mycompany.flightapp.controller;

import com.mycompany.flightapp.dto.JwtResponse;
import com.mycompany.flightapp.dto.LoginRequest;
import com.mycompany.flightapp.dto.RegisterRequest;
import com.mycompany.flightapp.dto.UserDTO;
import com.mycompany.flightapp.model.User;
import com.mycompany.flightapp.service.UserService;
import com.mycompany.flightapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(request.getPassword());

        User user = userService.createUser(userDTO, "USER");
        String token = this.jwtUtil.generateJwtToken(user.getUsername(),"USER");

         JwtResponse response = JwtResponse.builder()
                .token(token)
                .build();
         return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JwtResponse>  registerAdmin(@RequestBody RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(request.getPassword());

        User user = userService.createUser(userDTO, "ADMIN");
        String token = this.jwtUtil.generateJwtToken(user.getUsername(),"ADMIN");

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .expiryDate(jwtUtil.getExpirationDateFromJwtToken(token))
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            doAuthenticate(request.getUsername(), request.getPassword());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        String token = this.jwtUtil.generateJwtToken(userDetails.getUsername(), role);
        Date expiry = jwtUtil.getExpirationDateFromJwtToken(token);

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .expiryDate(expiry)
                .build();

        return ResponseEntity.ok(response);
    }


    private void doAuthenticate(String username, String password) {
        try {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
