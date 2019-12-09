package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.CompanySystemVersion;
import com.zhjs.scfcloud.model.mapper.CompanySystemVersionMapper;
import com.zhjs.scfcloud.system.service.CompanySystemVersionService;
import org.springframework.stereotype.Service;

@Service
public class CompanySystemVersionServiceImpl extends ServiceImpl<CompanySystemVersionMapper, CompanySystemVersion> implements CompanySystemVersionService {

}
