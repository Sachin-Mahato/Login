package com.sachin.Login.service;

import java.util.List;

import com.sachin.Login.domain.dtos.FileDto;
import com.sachin.Login.domain.entities.FileModel;

public interface FileService {
    FileModel saveFile(FileModel file);

    List<FileDto> getAllFiles();

}
