package com.bma.CloudFileStorage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {
    @Value("user-files")
    private String bucketName;

    private MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadFile(String fileName, String contentType, InputStream inputStream) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build()
        );


    }

    public void uploadFolder(MultipartFile multipartFile) {

    }
}
