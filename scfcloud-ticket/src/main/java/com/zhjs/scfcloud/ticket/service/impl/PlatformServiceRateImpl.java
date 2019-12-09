package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.entity.PlatformServiceRate;
import com.zhjs.scfcloud.model.mapper.PlatformServiceRateMapper;
import com.zhjs.scfcloud.ticket.service.PlatformServiceRateService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class PlatformServiceRateImpl extends ServiceImpl<PlatformServiceRateMapper, PlatformServiceRate> implements PlatformServiceRateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PlatformServiceRateMapper platformServiceRateMapper;


    //保存费率
    @Override
    public Result save(BaseSingleUpdateDTO dto) {
        PlatformServiceRate platformServiceRate = platformServiceRateMapper.selectOne(new QueryWrapper<PlatformServiceRate>().lambda()
                .eq(PlatformServiceRate::getSystemId, 2)
                .eq(PlatformServiceRate::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus())
                .last("limit 1"));
        if (platformServiceRate == null) {
            platformServiceRate = new PlatformServiceRate();
            platformServiceRate.setSystemId(2L);
            platformServiceRate.setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus());
            platformServiceRate.setRate(new BigDecimal(dto.getValue()));
            platformServiceRate.setCreateBy(dto.getUpdateBy());
            platformServiceRate.setCreateTime(new Date());
            if ( platformServiceRateMapper.insert(platformServiceRate) != 1) {
                return Result.failure("新增失败");
            }
        } else {
            platformServiceRate.setRate(new BigDecimal(dto.getValue()));
            platformServiceRate.setUpdateBy(dto.getUpdateBy());
            platformServiceRate.setUpdateTime(new Date());
            if ( platformServiceRateMapper.updateById(platformServiceRate) != 1) {
                return Result.failure("修改失败");
            }
        }
        return Result.success();
    }

    //查询费率
    @Override
    public Result query() {
        PlatformServiceRate platformServiceRate = platformServiceRateMapper.selectOne(new QueryWrapper<PlatformServiceRate>().lambda()
                .eq(PlatformServiceRate::getSystemId, 2)
                .eq(PlatformServiceRate::getDeleteStatus, CommonEnum.WhetherEnum.NO.getStatus())
                .last("limit 1"));
        if (platformServiceRate == null) {
            return Result.successData(0);
        }
        return Result.successData(platformServiceRate.getRate());
    }
}
