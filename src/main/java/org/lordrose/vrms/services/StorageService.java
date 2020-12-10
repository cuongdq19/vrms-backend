package org.lordrose.vrms.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file);

    String uploadFiles(MultipartFile[] files);
}
