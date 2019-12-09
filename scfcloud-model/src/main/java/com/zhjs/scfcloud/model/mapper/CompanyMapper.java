package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.vo.CompanyListVO;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司信息表 Mapper 接口.
 */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 公司管理列表查询 分页查询
     * 根据 状态 或者 名称模糊查询
     * @param page 分页参数 入参
     * @param companyName 公司名称 入参
     * @param status 状态 入参
     * @return
     */
    List<CompanyListVO> selectCompanyListPage(Page<CompanyListVO> page, @Param("companyName") String companyName, @Param("status") List<Integer> status);

    /**
     * 查询公司管理列表的全部数据 不分页
     * @return
     */
    List<CompanyListVO> findCompanyList();

    /**
     * 查询可指定报价公司列表
     * @param companyId 询价公司id
     * @return
     */
    List<CompanySelectVO> getQuotationCompanyList(Long companyId);
}