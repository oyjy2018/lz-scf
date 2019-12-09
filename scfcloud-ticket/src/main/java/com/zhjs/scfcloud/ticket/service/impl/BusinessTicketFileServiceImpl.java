package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import com.zhjs.scfcloud.model.mapper.BusinessTicketFileMapper;
import com.zhjs.scfcloud.ticket.service.BusinessTicketFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 商票文件
 */

@Service
public class BusinessTicketFileServiceImpl extends ServiceImpl<BusinessTicketFileMapper, BusinessTicketFile> implements BusinessTicketFileService {

    private Logger logger = LoggerFactory.getLogger(BusinessTicketFileServiceImpl.class);
}
