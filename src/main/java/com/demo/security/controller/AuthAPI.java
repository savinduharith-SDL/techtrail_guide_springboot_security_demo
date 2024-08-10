package com.demo.security.controller;

import com.demo.security.configuration.JwtAuthenticationController;
import com.demo.security.configuration.JwtRequest;
import com.demo.security.configuration.JwtResponse;
import com.demo.security.dto.AuthDto;
import com.demo.security.dto.ResponseDto;
import com.demo.security.dto.UserDto;
import com.demo.security.entity.User;
import com.demo.security.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/api/v1")
public class AuthAPI {

    @Autowired
    private JwtAuthenticationController jwtAuthenticationController;

    @Autowired
    private AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto> signIn(@RequestBody @Valid AuthDto authDto) throws DisabledException,BadCredentialsException{

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(authDto.getEmail());
        jwtRequest.setPassword(authDto.getPassword());

        JwtResponse jwtResponse = jwtAuthenticationController.generateAuthenticationToken(jwtRequest);

        User user = authService.findUserByEmail(authDto.getEmail());
        Map<String,Object> map = new HashMap<>();
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());
        map.put("role", user.getRole());
        map.put("email", user.getEmail());
        map.put("token", jwtResponse.getToken());

        return new ResponseEntity<>(new ResponseDto("success","200", map),HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody UserDto userDto) throws Exception{
        return new ResponseEntity<>(authService.signUp(userDto), HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request) throws Exception{

        String refreshedToken = jwtAuthenticationController.getRefreshToken(request);
        return new ResponseEntity<>(new ResponseDto("success","200",refreshedToken), HttpStatus.OK);
    }
}
