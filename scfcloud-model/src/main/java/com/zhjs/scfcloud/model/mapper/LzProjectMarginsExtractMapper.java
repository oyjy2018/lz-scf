package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectMarginsExtract;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectMarginsExtractVO;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-01 13:29
 */
public interface LzProjectMarginsExtractMapper extends BaseMapper<LzProjectMarginsExtract> {
    List<LzProjectMarginsExtractVO> findMarginsExtractList(String contractNo);
}
