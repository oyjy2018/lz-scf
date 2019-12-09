package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-29 11:39
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_file")
public class BusinessTicketFile {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 报价(商票)id
     * inquire_id
     */
    private Long inquireId;

    /**
     * ticket_front:票据正面图片，ticket_back:票据反面图片，trade_data:交易资料
     * file_type
     */
    private String fileType;

    /**
     * 排序
     * sort
     */
    private Integer sort;

    /**
     * 删除状态(0:未删,1:已删)
     * is_del
     */
    private Byte isDel;

    /**
     * 文件地址
     * file_url
     */
    private String fileUrl;

    /**
     * 预览文件地址
     */
    @TableField(exist = false)
    private String viewFileUrl;

    /**
     * 下载文件地址
     */
    @TableField(exist = false)
    private String downloadFileUrl;

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

    /**
     * 文件原始名称
     * original_file_name
     */
    private String originalFileName;

    /**
     * 文件新名称
     * new_file_name
     */
    private String newFileName;

    /**
     * 文件大小
     * file_size
     */
    private String fileSize;

    /**
     * 文件后缀
     * suffix
     */
    private String suffix;

    public String getViewFileUrl() {
        return FileUtil.getViewFileUrl(this.fileUrl);
    }

    public String getDownloadFileUrl() {
        return FileUtil.getDownloadFileUrl(this.fileUrl);
    }
}