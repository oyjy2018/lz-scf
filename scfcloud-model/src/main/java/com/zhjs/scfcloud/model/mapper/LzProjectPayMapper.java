package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectPay;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectPayListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-10-30 09:29
 */
public interface LzProjectPayMapper extends BaseMapper<LzProjectPay> {

    List<LzProjectPayListVO> getPayList(@Param("contractNo") String contractNo);
}