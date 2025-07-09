package com.sachin.Login.domain.dtos;

import java.util.UUID;

public record UserDto(UUID id, String username, String email) {

}
