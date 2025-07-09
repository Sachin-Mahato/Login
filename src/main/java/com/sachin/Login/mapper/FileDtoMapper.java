package com.sachin.Login.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.sachin.Login.domain.dtos.FileDto;
import com.sachin.Login.domain.entities.FileModel;

@Service
public class FileDtoMapper implements Function<FileModel, FileDto> {

    @Override
    public FileDto apply(FileModel file) {
        return new FileDto(
                file.getId(),
                file.getFileName(),
                file.getLanguage(),
                file.getSourceCode());
    }
}
