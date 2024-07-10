package com.bma.CloudFileStorage.services;

import com.bma.CloudFileStorage.models.dto.MinioResponseObjectDto;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {
    @Value("user-files")
    private String bucketName;

    private final MinioClient minioClient;

    public SearchService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public List<MinioResponseObjectDto> searchObjects(String owner, String query) {
        List<String> jopa = new ArrayList<>();



        Set<String> resultPaths = new HashSet<>();

        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(owner + "/")
                .recursive(true)
                .build());

        for(Result<Item> result : results){
            try {
                jopa.add(result.get().objectName());
                String path = result.get().objectName();
                String pathWithoutOwner = path.substring(path.indexOf("/") + 1);
                String[] parts = pathWithoutOwner.split("/");

                System.out.println();
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].contains(query)){
                        StringBuilder resultPath = new StringBuilder();
                        for (int j = 0; j <= i; j++) {
                            resultPath.append(parts[j]).append("/");
                        }
                        resultPaths.add(resultPath.toString());
                        System.out.println();
                    }
                }
            }catch (Exception e){
                //TODO exception handle
            }
        }
        List<MinioResponseObjectDto> searchResult = new ArrayList<>();

        for (String resultPath: resultPaths){
            String objectName = resultPath.substring(0, resultPath.length() -1 );
            System.out.println();
            if (objectName.contains("/")){
                int lastSlashIndex = objectName.lastIndexOf("/");
                objectName = objectName.substring(lastSlashIndex + 1);
                System.out.println();
            }
                if (resultPath.contains(".")){
                    //      DownloadedFolder/FileInFolder2.pptx/
                    resultPath = resultPath.substring(0, resultPath.length() - 1);
                    if (resultPath.contains("/")){
                        int lastSlashIndex = resultPath.lastIndexOf("/");
                        resultPath=resultPath.substring(0, lastSlashIndex);
                    }else {
                        resultPath="";
                    }

                    searchResult.add(new MinioResponseObjectDto(owner,resultPath, objectName, true));
                } else {
                    searchResult.add(new MinioResponseObjectDto(owner,resultPath, objectName, false));
                }


            System.out.println();
        }

        System.out.println();
        return searchResult;
    }

    private String truncatePath(String path, String query){


        return "";
    }
}
