package com.sachin.Login.service;

import com.sachin.Login.domain.dtos.UserDto;
import com.sachin.Login.domain.entities.UserModel;

public interface AuthService {
    public UserModel register(UserModel user);

    public String verify(UserModel user);

    public UserDto getMe(String email);

}
