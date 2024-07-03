package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.exceptions.FileStorageException;
import com.bma.CloudFileStorage.models.dto.DownloadFileRequestDto;
import com.bma.CloudFileStorage.models.dto.MinioResponseObjectDto;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MinioService {
    @Value("user-files")
    private String bucketName;

    private final MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadFile(MultipartFile file) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (file.getOriginalFilename().isEmpty()) {
            return;
        }

        String pathForCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName() + "/" + file.getOriginalFilename();

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
        for (MultipartFile file : folder) {
            uploadFile(file);
            ;
        }
    }


    public List<MinioResponseObjectDto> getFiles(String username, String path) {
        List<MinioResponseObjectDto> objects = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(username + "/" + path)
                        .build()
        );

        try {
            for (Result<Item> result : results) {
                if (result.get().objectName().contains(".")) {
                    String fullPath = result.get().objectName();
                    int lastSlashIndex = fullPath.lastIndexOf("/");
                    int firstSlashIndex = fullPath.indexOf("/");

                    String resultPath = fullPath.substring(firstSlashIndex + 1);
                    if (fullPath.endsWith("/")) {
                        fullPath = fullPath.substring(0, fullPath.length() - 1);
                    }


                    MinioResponseObjectDto minioResponseFileDto = new MinioResponseObjectDto(username, resultPath,
                            fullPath.substring(lastSlashIndex + 1), true);


                    objects.add(minioResponseFileDto);


                } else {
                    String fullPath = result.get().objectName();
                    int firstSlashIndex = fullPath.indexOf("/");
                    String resultPath = fullPath.substring(firstSlashIndex + 1);

                    if (fullPath.endsWith("/")) {
                        fullPath = fullPath.substring(0, fullPath.length() - 1);
                    }

                    int lastSlashIndex = fullPath.lastIndexOf("/");

                    MinioResponseObjectDto minioResponseFolderDto = new MinioResponseObjectDto(username, resultPath, fullPath.substring(lastSlashIndex + 1), false);

                    objects.add(minioResponseFolderDto);
                }
            }

        } catch (Exception e) {
            throw new FileStorageException("Something wrong with file storage");
        }

        return objects;
    }


//    public InputStreamResource downloadFile(DownloadFileRequestDto downloadFileRequestDto) {
//
//        try(
//                InputStream stream = minioClient.getObject(
//                        GetObjectArgs.builder()
//                                .bucket(bucketName)
//                                .object(downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath())
//                                .build()
//                )
//                ){
//            return new InputStreamResource(stream);
//        } catch (Exception e) {
//            throw new RuntimeException("Error occurred: " + e.getMessage());
//        }
//    }

    public Mono<InputStreamResource> download(DownloadFileRequestDto downloadFileRequestDto) {
        return Mono.fromCallable(() -> {
            InputStream response = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath()).build());
            return new InputStreamResource(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
