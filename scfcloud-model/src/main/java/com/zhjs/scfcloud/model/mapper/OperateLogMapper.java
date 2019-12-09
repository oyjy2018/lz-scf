package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.entity.OperateLog;
import com.zhjs.scfcloud.model.vo.OperateLogVO;

import java.util.List;

/**
 * 操作日志 Mapper 接口
 * @author:liuchanghai
 * @date:2019-06-21 16:32
 */
public interface OperateLogMapper extends BaseMapper<OperateLog> {
    int deleteByPrimaryKey(Long id);

    int insert(OperateLog record);

    int insertSelective(OperateLog record);

    OperateLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperateLog record);

    int updateByPrimaryKey(OperateLog record);

    List<OperateLogVO> getOperateLogVOList(OperateLogDTO dto);
}