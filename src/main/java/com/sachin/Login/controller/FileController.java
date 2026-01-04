package com.sachin.Login.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sachin.Login.domain.dtos.FileDto;
import com.sachin.Login.domain.entities.FileModel;
import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.security.UserPrincipal;
import com.sachin.Login.service.FileService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public ResponseEntity<?> createFile(@RequestBody FileModel file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to create a file.");
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserModel currentUser = userPrincipal.getUserModel();

        file.setUser(currentUser);
        FileModel savedFile = fileService.saveFile(file);
        if (savedFile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is corrupted");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("File has been saved successfully.");
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        var allFiles = fileService.getAllFiles();
        if (allFiles == null || allFiles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(allFiles);
    }

    @PatchMapping("/files/{id}")
    public ResponseEntity<?> updateFileSourceCode(@PathVariable UUID id, @RequestBody Map<String, Object> fields) {
        fileService.updateSourceCode(id, fields);
        return ResponseEntity.status(HttpStatus.OK).body("source code update successfully.");
    }

}
