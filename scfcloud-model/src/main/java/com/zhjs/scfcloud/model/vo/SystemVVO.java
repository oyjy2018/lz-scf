package com.zhjs.scfcloud.model.vo;

import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:25
 *
 * @author liuchanghai
 * @create 2019-05-23 11:25
 * @since
 */

@Data
public class SystemVVO extends BaseIdDTO {

    private String systemName;

    private String systemVersion;
}
