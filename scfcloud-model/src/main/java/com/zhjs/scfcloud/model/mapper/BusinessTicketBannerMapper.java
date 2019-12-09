package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.banner.BannerListDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketBanner;
import com.zhjs.scfcloud.model.vo.banner.BannerListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-18 15:06
 */
public interface BusinessTicketBannerMapper extends BaseMapper<BusinessTicketBanner> {
    List<BannerListVO> list(BannerListDTO dto);

    int updateGeSort(@Param("sort") Integer sort);

    int updateAddSort(@Param("beginSort")Integer beginSort, @Param("endSort")Integer endSort);

    int updateSubtractSort(@Param("beginSort")Integer beginSort, @Param("endSort")Integer endSort);
}