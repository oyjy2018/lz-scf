package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import com.zhjs.scfcloud.model.entity.LzProjectBasicInfo;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectListVO;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-10-24 11:23
 */
public interface LzProjectBasicInfoMapper extends BaseMapper<LzProjectBasicInfo> {

    List<LzProjectListVO> list(LzProjectListDTO dto);
}