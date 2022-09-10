package com.alkemy.disney.auth.services;

import com.alkemy.disney.auth.dto.UserDTO;
import com.alkemy.disney.auth.entities.UserEntity;
import com.alkemy.disney.auth.repositories.UserRepository;
import com.alkemy.disney.exception.EnumErrors;
import com.alkemy.disney.exception.UserRegisterError;
import com.alkemy.disney.exception.UserWrongLogin;
import com.alkemy.disney.services.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsCustomService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserWrongLogin {
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            throw new UserWrongLogin(EnumErrors.WRONG_CREDENTIALS.getErrorMessage());
        }


        return new User(userEntity.getUsername(), userEntity.getPassword(), Collections.emptyList());
    }

    public boolean save(UserDTO userDTO){
        if(userRepository.findByUsername(userDTO.getUsername()) !=null){
            throw new UserRegisterError(EnumErrors.USER_ALREADY_EXIST.getErrorMessage());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        userEntity = userRepository.save(userEntity);

        if(userEntity != null){
            emailService.sendWelcomeEmail(userEntity.getUsername());
        }; //email

        return userEntity != null;
    }


}