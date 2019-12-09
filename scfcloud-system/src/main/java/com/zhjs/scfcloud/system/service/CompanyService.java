package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.EditStatusDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyListDTO;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.vo.CompanyBasicInfoVO;
import com.zhjs.scfcloud.model.vo.CompanyListVO;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import com.zhjs.scfcloud.model.vo.UserVO;

import java.util.List;

/**
 * 公司管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 17:01
 *
 * @author liuchanghai
 * @create 2019-05-21 17:01
 * @since
 */

public interface CompanyService extends IService<Company> {

    /**
     * 公司管理列表
     * @param dto
     * @return
     */
    Page<CompanyListVO> selectCompanyListPage(FindCompanyListDTO dto);

    /**
     * 获取公司详情
     * @param companyId
     * @return
     */
    Result selectCompanyDetail(Long companyId);
    /**
     * 查询公司管理列表 不分页
     * @param dto
     * @return
     */
    List<CompanyListVO> findList(FindCompanyListDTO dto);

    /**
     * 编辑公司状态
     * @param dto 入参
     * @return
     */
    boolean editComapnyStatusById(EditStatusDTO dto);

    /**
     * 根据公司ID查询公司的所有成员
     * @param companyId 入参
     * @return
     */
    List<UserVO> findCompanyUserListById(Long companyId);

    /**
     * 根据用户ID查询用户加入的公司
     * @param userId 入参 用户ID
     * @return
     */
    List<CompanySelectVO> findUserDataPermissionList(Long userId);

    /**
     * 删除公司(逻辑删除)
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 公司是否存在
     * @param comapnyName
     * @return
     */
    boolean isExist(String comapnyName);

    CompanyBasicInfoVO findCompanyBasicInformation(Long companyId);

    Result isJdVerified(Long companyId);
}
