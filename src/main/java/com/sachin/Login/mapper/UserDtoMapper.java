package com.sachin.Login.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.sachin.Login.domain.dtos.UserDto;
import com.sachin.Login.domain.entities.UserModel;

@Service
public class UserDtoMapper implements Function<UserModel, UserDto> {
    @Override
    public UserDto apply(UserModel user) {

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail());
    }
}
