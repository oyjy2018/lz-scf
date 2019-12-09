package com.zhjs.scfcloud.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 操作文件 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 14:57
 *
 * @author liuchanghai
 * @create 2019-06-13 14:57
 * @since
 */

@Data
@Accessors(chain = true)
public class FileDTO implements Serializable {

    private Long id;

    /**
     * 文件CODE
     */
    private String fileCode;

    /**
     * 新的文件名称
     */
    private String newFileName;

    /**
     * 原始文件名称
     */
    private String originalFileName;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件状态
     */
    private Integer status;

    /**
     * 文件后缀名
     */
    private String suffix;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 业务类型Id
     */
    private Long businessTypeId;

    /**
     * 文件种类
     */
    private String fileSpecies;

    /**
     * 文件CODE集合
     */
    private String fileCodes;
}
