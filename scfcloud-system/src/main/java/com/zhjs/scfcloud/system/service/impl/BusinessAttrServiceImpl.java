package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.BusinessAttr;
import com.zhjs.scfcloud.model.mapper.BusinessAttrMapper;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.system.service.BusinessAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/12 16:53
 */
@Service
public class BusinessAttrServiceImpl extends ServiceImpl<BusinessAttrMapper, BusinessAttr> implements BusinessAttrService {

    @Autowired
    private BusinessAttrMapper businessAttrMapper;


    @Override
    public List<BusinessAttrCfgVO> findBusinessAttrCfg() {
        return businessAttrMapper.findBusinessAttrCfg();
    }
}
