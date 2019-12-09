package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.LzProjectInvoice;
import com.zhjs.scfcloud.model.vo.lzProject.LzProjectInvoiceListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-11-14 16:00
 */
public interface LzProjectInvoiceMapper extends BaseMapper<LzProjectInvoice> {

    List<LzProjectInvoiceListVO> getList(@Param("contractNo") String contractNo);
}