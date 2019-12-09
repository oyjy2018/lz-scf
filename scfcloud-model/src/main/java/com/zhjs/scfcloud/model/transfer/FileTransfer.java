package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.entity.File;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/25 17:04
 */
@Mapper(componentModel = "spring")
public interface FileTransfer {

    List<File> toFileList(List<FileDTO> fileDTOList);

    File toFile(FileDTO dto);
}
