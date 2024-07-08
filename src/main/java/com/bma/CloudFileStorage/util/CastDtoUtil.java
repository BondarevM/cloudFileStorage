package com.bma.CloudFileStorage.util;

import com.bma.CloudFileStorage.exceptions.IllegalFileNameException;
import com.bma.CloudFileStorage.models.dto.RenameObjectRequestDto;
import com.bma.CloudFileStorage.models.dto.ObjectRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CastDtoUtil {

    public RenameObjectRequestDto castToRenameFileDto(ObjectRequestDto dto){
        if (dto.getName().contains("/")){
            throw new IllegalFileNameException("File name must not contain '/'");
        }

        RenameObjectRequestDto result = new RenameObjectRequestDto();
        result.setOwner(dto.getOwner());
        result.setSourcePath(dto.getPath());


        String newPath = dto.getName();

        if (dto.getPath().contains("/")) {
            int lastSlashIndex = dto.getPath().lastIndexOf("/");
            newPath = dto.getPath().substring(0, lastSlashIndex) + "/" + dto.getName();
        }
        result.setNewPath(newPath);
        return result;
    }
}
