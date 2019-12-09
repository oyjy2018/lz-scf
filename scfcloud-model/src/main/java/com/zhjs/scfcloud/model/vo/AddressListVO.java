package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:17
 *
 * @author liuchanghai
 * @create 2019-05-23 14:17
 * @since
 */
@Data
public class AddressListVO {

    @ApiModelProperty("地址ID")
    private String id;

    @ApiModelProperty("地址名称")
    private String name;

    @ApiModelProperty("下级地址")
    private List<AddressListVO> children;

}
