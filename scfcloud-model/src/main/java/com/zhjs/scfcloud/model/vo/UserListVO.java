package com.zhjs.scfcloud.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 12:59
 *
 * @author liuchanghai
 * @create 2019-05-23 12:59
 * @since
 */

@Data
public class UserListVO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("用户组名称")
    private String roleName;

    @ApiModelProperty("公司用户ID")
    private Integer companyUserId;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("申请加入时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
