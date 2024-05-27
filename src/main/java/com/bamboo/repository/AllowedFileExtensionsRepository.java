package com.bamboo.repository;

import com.bamboo.entity.AllowedFileExtensions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowedFileExtensionsRepository extends JpaRepository<AllowedFileExtensions, Long> {
    List<AllowedFileExtensions> findAllByFileConfigId(Long fileConfigId);
}
