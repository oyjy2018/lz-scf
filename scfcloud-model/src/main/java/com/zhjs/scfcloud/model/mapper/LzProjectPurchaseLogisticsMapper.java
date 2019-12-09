package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectPurchaseLogistics;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectPurchaseLogisticsListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-04 14:14
 */
public interface LzProjectPurchaseLogisticsMapper extends BaseMapper<LzProjectPurchaseLogistics> {
    //通过合同号获取采购物流信息
    List<LzProjectPurchaseLogisticsListVO> getListByContractNo(@Param("contractNo") String contractNo);
}