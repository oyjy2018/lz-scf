package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.AttrValDTO;
import com.zhjs.scfcloud.model.dto.business.QueryAttrValDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;

/**
 * 业务字段属性值管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 14:17
 *
 * @author liuchanghai
 * @create 2019-06-12 14:17
 * @since
 */

public interface BusinessAttrValService extends IService<BusinessAttrVal> {

    /**
     * 根据 业务类型ID 或 业务属性ID 业务字段属性值
     * @param dto 入参
     * @return
     */
    Result findByTypeIdOrAttrId(QueryAttrValDTO dto);

    /**
     * 新建
     * @param dto 入参
     * @return
     */
    Result add(AttrValDTO dto);
}
