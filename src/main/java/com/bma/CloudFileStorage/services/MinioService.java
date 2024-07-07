package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.exceptions.FileStorageException;
import com.bma.CloudFileStorage.models.dto.CreateEmptyFolderDto;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class MinioService {
    @Value("user-files")
    private String bucketName;

    private final MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadFile(MultipartFile file, String path) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (file.getOriginalFilename().isEmpty()) {
            return;
        }

        String pathForCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName() + "/" + path + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(pathForCurrentUser)
                        .stream(file.getInputStream(), file.getInputStream().available(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    public void uploadFolder(List<MultipartFile> folder, String path) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        for (MultipartFile file : folder) {
            uploadFile(file, path);

        }
    }


    public List<MinioResponseObjectDto> getFiles(String username, String path) {
        List<MinioResponseObjectDto> objects = new ArrayList<>();
        if (!path.isEmpty() && !path.endsWith("/")) {
            path = path + "/";
        }

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
                    if (fullPath.endsWith("/")) {
                        fullPath = fullPath.substring(0, fullPath.length() - 1);
                    }
                    int lastSlashIndex = fullPath.lastIndexOf("/");
                    int firstSlashIndex = fullPath.indexOf("/");

                    String resultPath = fullPath.substring(firstSlashIndex + 1);

                    MinioResponseObjectDto minioResponseFileDto = new MinioResponseObjectDto(username, resultPath, fullPath.substring(lastSlashIndex + 1), true);

                    objects.add(minioResponseFileDto);


                } else {
                    String fullPath = result.get().objectName();
                    if (fullPath.endsWith("/")) {
                        fullPath = fullPath.substring(0, fullPath.length() - 1);
                    }
                    int lastSlashIndex = fullPath.lastIndexOf("/");
                    int firstSlashIndex = fullPath.indexOf("/");
                    String resultPath = fullPath.substring(firstSlashIndex + 1);

                    MinioResponseObjectDto minioResponseFolderDto = new MinioResponseObjectDto(username, resultPath, fullPath.substring(lastSlashIndex + 1), false);

                    objects.add(minioResponseFolderDto);
                }
            }

        } catch (Exception e) {

            throw new FileStorageException("Something wrong with file storage");
        }

        return objects;
    }


//    public Mono<InputStreamResource> downloadFile(DownloadFileRequestDto downloadFileRequestDto) {
//        return Mono.fromCallable(() -> {
//            InputStream response = minioClient.getObject(GetObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath())
//                    .build());
//            return new InputStreamResource(response);
//        }).subscribeOn(Schedulers.boundedElastic());
//    }

    public GetObjectResponse downloadFile(DownloadFileRequestDto downloadFileRequestDto) {

        GetObjectResponse response = null;
        try {
            response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath())
                    .build());
        } catch (Exception e) {
            //TODO error handle
        }
        return response;


    }

    public void createEmptyFolder(String path, CreateEmptyFolderDto createEmptyFolderDto) {
        String pathForCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName() + "/" + path + createEmptyFolderDto.getName() + "/";
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(pathForCurrentUser).stream(
                                    new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build());
        } catch (Exception e) {
            // TODO error handle
            System.out.println();
        }
    }


    public ByteArrayOutputStream downloadFolder(DownloadFileRequestDto downloadFileRequestDto) {
        List<DownloadFileRequestDto> allObjects = new ArrayList<>();

        String prefix = downloadFileRequestDto.getOwner() + "/" + downloadFileRequestDto.getPath();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName)
                        .prefix(prefix).recursive(true).build());

        for (Result<Item> result : results) {
            try {

                String path = result.get().objectName();
                allObjects.add(new DownloadFileRequestDto("", path, ""));
            } catch (Exception e) {
                //TODO error handle
            }
        }


        List<GetObjectResponse> result = new ArrayList<>();
        for (DownloadFileRequestDto req : allObjects) {
            result.add(downloadFile(req));
        }
        return zipFiles(result, downloadFileRequestDto.getName());

    }

    private ByteArrayOutputStream zipFiles(List<GetObjectResponse> objects, String folderName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (GetObjectResponse object : objects) {
                zos.putNextEntry(new ZipEntry(object.object().substring(object.object().indexOf(folderName))));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = object.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            }

        } catch (Exception e) {
            //TODO handle exception
        }
        return baos;
    }
}
