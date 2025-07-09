package com.sachin.Login.domain.dtos;

import java.util.UUID;

import com.sachin.Login.domain.Language;

public record FileDto(UUID id, String fileName, Enum<Language> language, String sourceCode) {

}
