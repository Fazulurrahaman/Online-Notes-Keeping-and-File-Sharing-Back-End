package com.notes.controller;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notes.models.JwtAuthResponse;
import com.notes.models.LoginDto;
import com.notes.models.User;
import com.notes.repository.UserRepository;
import com.notes.service.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private AuthService authService;
	


    // Build Login REST API
    @PostMapping(value ="/login" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
    
    @PostMapping(value ="/signin" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> signin(@RequestBody User userRequest){
    	 try {
             User userResponse = authService.saveUser(userRequest);
             return ResponseEntity.ok(userResponse);
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

}