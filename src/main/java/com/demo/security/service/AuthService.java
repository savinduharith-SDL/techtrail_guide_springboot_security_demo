package com.demo.security.service;

import com.demo.security.constants.AppConstants;
import com.demo.security.dto.ResponseDto;
import com.demo.security.dto.UserDto;
import com.demo.security.exception.types.EmailNotFoundException;
import com.demo.security.exception.types.EmailNotValidException;
import com.demo.security.configuration.JwtAuthenticationController;
import com.demo.security.repository.AuthRepository;
import com.demo.security.entity.User;
import com.demo.security.util.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationController authenticationController;

    public User findValidUserByEmail(String email) {
        return authRepository.findFirstByEmailAndActiveTrueAndVerifiedTrue(email);
    }

    public User findUserByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto signUp(UserDto userDto) throws Exception{
        User user = findUserByEmail(userDto.getEmail());
        if (user != null) {
            log.info("{}. tried to sign up again",user.getEmail());
            return new ResponseDto("500","That email is taken. Try another one!");
        } else {
            String verifyCode = new Utilities().generateRandomNumber();

            user = new ModelMapper().map(userDto, User.class);
            user.setActive(true);
            user.setVerified(false);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setVerifyCode(verifyCode);
            user.setCreateDateTime(new Date());
            user = authRepository.saveAndFlush(user);
            return new ResponseDto("success","200",null);
        }
    }

}
