package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.BusinessAttr;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/12 16:51
 */
public interface BusinessAttrService extends IService<BusinessAttr> {
    /**
     * 查询业务属性配置
     * @return
     */
    List<BusinessAttrCfgVO> findBusinessAttrCfg();
}
