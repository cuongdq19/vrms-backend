package org.lordrose.vrms.services.impl;

import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {



    @Override
    public String uploadFile(MultipartFile file) {
        return "";
    }

    @Override
    public String uploadFiles(MultipartFile[] files) {
        return "";
    }
}
