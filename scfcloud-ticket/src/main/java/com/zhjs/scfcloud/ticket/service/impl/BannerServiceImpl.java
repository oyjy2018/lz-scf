package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerAddDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerListDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerSortDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerUpdateDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketBanner;
import com.zhjs.scfcloud.model.mapper.BusinessTicketBannerMapper;
import com.zhjs.scfcloud.model.vo.banner.BannerDetailVO;
import com.zhjs.scfcloud.model.vo.banner.BannerListVO;
import com.zhjs.scfcloud.ticket.service.BannerService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BannerServiceImpl extends ServiceImpl<BusinessTicketBannerMapper, BusinessTicketBanner> implements BannerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private BusinessTicketBannerMapper bannerMapper;

    @Override
    public Result list(BannerListDTO dto) {
        List<BannerListVO> list = bannerMapper.list(dto);
        return Result.success(list);
    }

    @Override
    public Result add(BannerAddDTO dto) {
        BusinessTicketBanner banner = new BusinessTicketBanner();
        //查询当前最大排序值
        BusinessTicketBanner maxSortBanner = bannerMapper.selectOne(new QueryWrapper<BusinessTicketBanner>().lambda()
                .ne(BusinessTicketBanner::getDeleteStatus,CommonEnum.WhetherEnum.YES.getStatus())
                .orderByDesc(BusinessTicketBanner::getSort)
                .last("limit 1"));

        banner.setBannerFileUrl(dto.getBannerFileUrl())
                .setBannerName(dto.getBannerName())
                .setJumpUrl(dto.getJumpUrl())
                .setSort(maxSortBanner == null ? 1 : maxSortBanner.getSort()+1)
                .setUseStatus(Integer.parseInt(dto.getUseStatus()))
                .setDeleteStatus(CommonEnum.WhetherEnum.NO.getStatus())
                .setCreateBy(dto.getUserId()).setUpdateBy(dto.getUserId())
                .setCreateTime(new Date()).setUpdateTime(new Date());

        if (bannerMapper.insert(banner) != 1) {
            return Result.failure("插入失败");
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result delete(Long id, Long userId) {
        BusinessTicketBanner banner = bannerMapper.selectById(id);
        if (banner == null) {
            return Result.failure("banner信息不存在");
        }
        //修改排序大于被删除banner的banner的排序值
        bannerMapper.updateGeSort(banner.getSort());
        banner.setDeleteStatus(CommonEnum.WhetherEnum.YES.getStatus())
                .setUseStatus(CommonEnum.WhetherEnum.NO.getStatus())
                .setSort(null)
                .setUpdateBy(userId).setUpdateTime(new Date()); //将删除的banner排序设为空
        if (bannerMapper.updateById(banner) != 1) {
            return Result.failure("删除失败");
        }

        return Result.success();
    }

    /**
     * 启用/禁用banner
     * @param dto
     * @return
     */
    @Override
    public Result updateUseStatus(BaseSingleUpdateDTO dto) {
        BusinessTicketBanner banner = bannerMapper.selectById(dto.getId());
        if (banner == null) {
            return Result.failure("banner信息不存在");
        }
        banner.setUseStatus(Integer.parseInt(dto.getValue()))
                .setUpdateBy(dto.getUpdateBy()).setUpdateTime(new Date());
        if (bannerMapper.updateById(banner) != 1) {
            return Result.failure("修改失败");
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result sort(BannerSortDTO dto) {
        BusinessTicketBanner banner = bannerMapper.selectById(dto.getId());
        if (banner == null) return Result.failure("原位置banner不存在");
        BusinessTicketBanner toTanner = bannerMapper.selectById(dto.getToId());
        if (toTanner == null) return Result.failure("目标位置banner不存在");
        if ("up".equals(dto.getDirection())) {
            //将两个banner之间的banner排序全部加1
            bannerMapper.updateAddSort(toTanner.getSort(),banner.getSort());

        } else if ("down".equals(dto.getDirection())) {
            //将两个banner之间的banner排序全部减1
            bannerMapper.updateSubtractSort(banner.getSort(),toTanner.getSort());
        } else {
            return Result.failure("移动方向取值不合规");
        }

        //设为目标位置的排序
        banner.setSort(toTanner.getSort()).setUpdateBy(dto.getUserId()).setUpdateTime(new Date());
        //更新原位置banner
        bannerMapper.updateById(banner);

        return Result.success();
    }

    @Override
    public Result detail(Long id) {
        BusinessTicketBanner banner = bannerMapper.selectById(id);
        if (banner == null) return Result.failure("banner不存在");
        BannerDetailVO detail = JsonUtil.jsonToBean(JsonUtil.toJSON(banner),BannerDetailVO.class);
        return Result.success(detail);
    }

    @Override
    public Result update(BannerUpdateDTO dto) {
        BusinessTicketBanner banner = bannerMapper.selectById(dto.getId());
        if (banner == null) return Result.failure("banner不存在");

        banner.setBannerName(dto.getBannerName());
        banner.setBannerFileUrl(dto.getBannerFileUrl());
        banner.setUseStatus(Integer.parseInt(dto.getUseStatus()));
        banner.setJumpUrl(dto.getJumpUrl());
        banner.setUpdateBy(dto.getUserId());
        banner.setUpdateTime(new Date());
        if (bannerMapper.updateById(banner) != 1) {
            return Result.failure("更新失败");
        }
        return Result.success();
    }

    /**
     * 首页获取轮播图
     * @return
     */
    @Override
    public Result getCarousel() {
        //查询所有有效banner
        List<BusinessTicketBanner> list = bannerMapper.selectList(new QueryWrapper<BusinessTicketBanner>().lambda()
                .ne(BusinessTicketBanner::getDeleteStatus,CommonEnum.WhetherEnum.YES.getStatus())
                .eq(BusinessTicketBanner::getUseStatus,CommonEnum.WhetherEnum.YES.getStatus())
                .orderByAsc(BusinessTicketBanner::getSort));
        List<Map> result = new ArrayList<>();
        if (!ListUtil.isEmpty(list)) {
            for (BusinessTicketBanner banner : list) {
                Map map = new HashMap();
                map.put("bannerFileUrl",FileUtil.getViewFileUrl(banner.getBannerFileUrl()));
                map.put("jumpUrl",banner.getJumpUrl());
                result.add(map);
            }
        }
        return Result.success(result);
    }
}
