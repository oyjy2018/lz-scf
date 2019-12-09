package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerAddDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerListDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerSortDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerUpdateDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketBanner;

/**
 * banner
 */
public interface BannerService extends IService<BusinessTicketBanner> {

    /**
     * banner列表
     * @param dto
     * @return
     */
    Result list(BannerListDTO dto);

    /**
     * 添加banner
     * @param dto
     * @return
     */
    Result add(BannerAddDTO dto);

    /**
     * 删除banner
     * @param id
     * @param userId
     * @return
     */
    Result delete(Long id, Long userId);

    /**
     * 启用/禁用banner
     * @param dto
     * @return
     */
    Result updateUseStatus(BaseSingleUpdateDTO dto);

    /**
     * 排序banner
     * @param dto
     * @return
     */
    Result sort(BannerSortDTO dto);

    /**
     * banner详情
     * @param id
     * @return
     */
    Result detail(Long id);

    /**
     * 修改banner
     * @param dto
     * @return
     */
    Result update(BannerUpdateDTO dto);

    /**
     * 首页获取轮播图
     * @return
     */
    Result getCarousel();
}
