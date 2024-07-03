package com.bma.CloudFileStorage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DownloadFileRequestDto {
    private String owner;
    private String path;
    private String name;
}
