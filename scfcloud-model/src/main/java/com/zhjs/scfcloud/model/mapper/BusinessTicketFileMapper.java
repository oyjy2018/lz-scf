package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;

/**
 * 报价(商票)文件表 Mapper 接口
 * @author:dailongting
 * @date:2019-07-29 11:39
 */
public interface BusinessTicketFileMapper extends BaseMapper<BusinessTicketFile> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessTicketFile record);

    int insertSelective(BusinessTicketFile record);

    BusinessTicketFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessTicketFile record);

    int updateByPrimaryKey(BusinessTicketFile record);
}