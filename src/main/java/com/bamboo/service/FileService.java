package com.bamboo.service;

import com.bamboo.dto.UpdateFileAllowedRequest;
import com.bamboo.entity.AllowedFileExtensions;
import com.bamboo.entity.FileConfig;
import com.bamboo.repository.AllowedFileExtensionsRepository;
import com.bamboo.repository.FileConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileConfigRepository fileConfigRepository;
    private final AllowedFileExtensionsRepository allowedFileExtensionsRepository;

    public UpdateFileAllowedRequest getFileConfig(Long fileConfigId) {
        FileConfig fileConfig = fileConfigRepository.findById(fileConfigId)
                .orElseThrow(() ->
                        new IllegalArgumentException("not found!"));

        List<AllowedFileExtensions> allowedFileExtensionsList = allowedFileExtensionsRepository.findAllByFileConfigId(fileConfigId);

        List<String> allowedFileExtension = new ArrayList<>();
        for (AllowedFileExtensions allowedFileExtensions: allowedFileExtensionsList) {
            String extension = allowedFileExtensions.getExtension();
            allowedFileExtension.add(extension);
        }

        UpdateFileAllowedRequest updateFileAllowedRequest = new UpdateFileAllowedRequest();
        updateFileAllowedRequest.setMaxFileSize(fileConfig.getMaxFileSize());
        updateFileAllowedRequest.setMaxFileCount(fileConfig.getMaxFileCount());
        updateFileAllowedRequest.setExtensions(allowedFileExtension);

        return updateFileAllowedRequest;
    }
}
