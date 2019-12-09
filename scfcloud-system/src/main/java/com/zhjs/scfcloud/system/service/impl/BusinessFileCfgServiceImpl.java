package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.BusinessFileCfg;
import com.zhjs.scfcloud.model.mapper.BusinessFileCfgMapper;
import com.zhjs.scfcloud.system.service.BusinessFileCfgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/19 18:44
 */
@Service
public class BusinessFileCfgServiceImpl extends ServiceImpl<BusinessFileCfgMapper, BusinessFileCfg> implements BusinessFileCfgService {

    @Resource
    BusinessFileCfgMapper businessFileCfgMapper;

}
