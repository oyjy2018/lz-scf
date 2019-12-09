package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 添加银行 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 16:00
 *
 * @author liuchanghai
 * @create 2019-05-27 16:00
 * @since
 */

@Data
public class AddBankDTO {

    private String bankNameCn;

    private String bankNameEn;

    private String bankType;
}
