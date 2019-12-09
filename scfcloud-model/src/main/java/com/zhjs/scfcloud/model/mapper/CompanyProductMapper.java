package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.CompanyProduct;

import java.util.List;

/**
 * 公司与产品关联关系信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 08:55
 *
 * @author liuchanghai
 * @create 2019-05-22 08:55
 * @since
 */

public interface CompanyProductMapper extends BaseMapper<CompanyProduct> {

    boolean insertByBatch(List<CompanyProduct> cps);
}
