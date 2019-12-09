package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectMarginsPay;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectMarginsPayVO;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-01 11:50
 */
public interface LzProjectMarginsPayMapper extends BaseMapper<LzProjectMarginsPay> {
    List<LzProjectMarginsPayVO> findMarginsPayList(String contractNo);
}
