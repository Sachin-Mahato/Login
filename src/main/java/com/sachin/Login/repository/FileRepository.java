package com.sachin.Login.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sachin.Login.domain.entities.FileModel;

@Repository
public interface FileRepository extends JpaRepository<FileModel, UUID> {

}
