package com.bma.CloudFileStorage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioService {
    @Value("user-files")
    private String bucketName;

    private MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadFile(MultipartFile file) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (file.getOriginalFilename().isEmpty()){
            return;
        }

        String pathForCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName() + "/"+ file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(pathForCurrentUser)
                        .stream(file.getInputStream(), file.getInputStream().available(), -1)
                        .contentType(file.getContentType())
                        .build()
        );


    }

    public void uploadFolder(List<MultipartFile> folder) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        for (MultipartFile file : folder){
            uploadFile(file);;
        }
    }
}
