package org.lordrose.vrms.services.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.lordrose.vrms.services.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

    private final StorageOptions storageOptions;
    private static final String bucketName = "vrms-290212.appspot.com";
    private Storage storage;

    @PostConstruct
    private void setStorage() {
        storage = storageOptions.getService();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return processUploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String uploadFiles(MultipartFile[] files) {
        StringBuilder resultCollector = new StringBuilder();
        for (MultipartFile file : files) {
            resultCollector.append(uploadFile(file));
        }
        return resultCollector.toString();
    }

    private String processUploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            return "";
        storage = storageOptions.getService();
        final String uuid = UUID.randomUUID().toString();
        final String path = uuid + multipartFile.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, path);
        BlobInfo info = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();
        Blob returnedBlob = storage.create(
                info, multipartFile.getInputStream().readAllBytes());
        URL url = storage.signUrl(
                BlobInfo.newBuilder(returnedBlob.getBlobId()).build(), 10000L, TimeUnit.DAYS
        );
        return url.toString() + "||";
    }
}
