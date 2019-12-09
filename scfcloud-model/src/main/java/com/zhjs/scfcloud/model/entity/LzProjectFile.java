package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-06 10:43
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_file")
public class LzProjectFile {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 项目合同号
     * contract_no
     */
    private String contractNo;

    /**
     * 文件分类（根据作用区别）
     * file_classify
     */
    private String fileClassify;

    /**
     * 文件新名称
     * new_file_name
     */
    private String newFileName;

    /**
     * 文件原始名称
     * original_file_name
     */
    private String originalFileName;

    /**
     * 文件url
     * file_url
     */
    private String fileUrl;

    /**
     * 文件大小
     * file_size
     */
    private String fileSize;

    /**
     * 文件类型（根据文件格式区分）
     * file_type
     */
    private String fileType;

    /**
     * 文件后缀名
     * suffix
     */
    private String suffix;

    /**
     * 上传时间
     * upload_time
     */
    private Date uploadTime;

    /**
     * 上传人
     * upload_person
     */
    private String uploadPerson;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;
}