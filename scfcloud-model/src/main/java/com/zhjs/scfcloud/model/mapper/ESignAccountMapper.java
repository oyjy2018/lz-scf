package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.ESignAccount;

/**
 * 公司对应e签宝账号信息表 Mapper 接口
 * @author:weijie.chen
 * @date:2019-07-17 18:05
 */
public interface ESignAccountMapper extends BaseMapper<ESignAccount> {
    int deleteByPrimaryKey(Integer id);

    int insert(ESignAccount record);

    int insertSelective(ESignAccount record);

    ESignAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ESignAccount record);

    int updateByPrimaryKeyWithBLOBs(ESignAccount record);

    int updateByPrimaryKey(ESignAccount record);
}