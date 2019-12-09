package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 14:00
 *
 * @author liuchanghai
 * @create 2019-06-22 14:00
 * @since
 */

@Data
public class CommentVO {

    private Long id;

    /**
     * 在状态
     */
    private String statusDesc;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;
}
