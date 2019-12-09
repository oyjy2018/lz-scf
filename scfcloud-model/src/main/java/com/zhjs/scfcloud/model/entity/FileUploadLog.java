package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文件上传日记信息表实体类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 12:14
 *
 * @author liuchanghai
 * @create 2019-06-13 12:14
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_file_upload_log")
public class FileUploadLog {

    /**
     * 主键ID
     */
    private Long id;

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
     * 文件后缀名
     */
    private String suffix;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;
}
