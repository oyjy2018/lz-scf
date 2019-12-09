package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.PropertiesUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文件信息表实体类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-13 12:03
 *
 * @author liuchanghai
 * @create 2019-06-13 12:03
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_file")
public class File {

    /**
     * 主键ID
     */
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
     * 文件预览地址
     */
    private transient String viewFileUrl;

    /**
     * 文件下载地址
     */
    private transient String downloadFileUrl;

    /**
     * 文件后缀名
     */
    private String suffix;

    /**
     * 文件状态
     */
    private Integer status;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private Long businessTypeId;

    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getViewFileUrl() {
        return FileUtil.getViewFileUrl(this.fileUrl);
    }

    public String getDownloadFileUrl() {
        return FileUtil.getDownloadFileUrl(this.fileUrl);
    }
}
