package com.sachin.Login.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sachin.Login.domain.dtos.FileDto;
import com.sachin.Login.domain.entities.FileModel;
import com.sachin.Login.mapper.FileDtoMapper;
import com.sachin.Login.repository.FileRepository;
import com.sachin.Login.service.FileService;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileDtoMapper fileDtoMapper;

    public FileServiceImpl(FileRepository fileRepository, FileDtoMapper fileDtoMapper) {
        this.fileRepository = fileRepository;
        this.fileDtoMapper = fileDtoMapper;
    }

    @Override
    public FileModel saveFile(FileModel file) {
        return fileRepository.save(file);
    }

    @Override
    public List<FileDto> getAllFiles() {
        return fileRepository
                .findAll().stream().map(fileDtoMapper).collect(Collectors.toList());
    }

    @Override
    public FileDto updateSourceCode(UUID id, Map<String, Object> fields) {
        FileModel existingSourceCode = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found" + id));

        fields.forEach((key, value) -> {
            if ("sourceCode".equals(key)) {
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("sourceCode must be string");
                }
                existingSourceCode.setSourceCode((String) value);
            } else {
                throw new IllegalArgumentException("Unsupported fields" + value);
            }
        });
        
        FileModel saveSourceCode = fileRepository.save(existingSourceCode);
        return fileDtoMapper.apply(saveSourceCode);
    }

}
