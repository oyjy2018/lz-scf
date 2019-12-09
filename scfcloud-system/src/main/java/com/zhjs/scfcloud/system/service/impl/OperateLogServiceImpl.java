package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.entity.OperateLog;
import com.zhjs.scfcloud.model.mapper.OperateLogMapper;
import com.zhjs.scfcloud.model.vo.OperateLogVO;
import com.zhjs.scfcloud.system.service.OperateLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日记业务管理接口的实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-20 11:36
 *
 * @author liuchanghai
 * @create 2019-06-20 11:36
 * @since
 */

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    @Resource
    private OperateLogMapper operateLogMapper;

    /**
     * 查询操作日志
     * @param dto
     * @return
     */
    @Override
    public String findList(OperateLogDTO dto) {
        Map retMap = new HashMap();
        List<OperateLogVO> operateLogVOList = operateLogMapper.getOperateLogVOList(dto);
        retMap.put("total",dto.getTotal());
        retMap.put("list",operateLogVOList);
        return Result.success(retMap).toSerializerJSON();
    }
}
