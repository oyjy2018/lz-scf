package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.CompanyUser;
import com.zhjs.scfcloud.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/5/22 11:33
 */
public interface CompanyUserMapper extends BaseMapper<CompanyUser> {


    Long findLastAccessCompanyIdByUserId(@Param("userId")Long userId);

    /**
     * 根据公司ID 查询公司的所有成员
     * @param companyId
     * @return
     */
    List<UserVO> findCompanyUserListById(@Param("companyId")Long companyId);

    /**
     * 查询用户加入的公司
     * @param userId 用户ID
     * @return
     */
    List<Long> findCompanyIdList(Long userId);

    /**
     * 查询员工在职状态
     * @param userId
     * @param companyId
     * @return
     */
    Integer findCompanyUserStatus(@Param("userId") Long userId, @Param("companyId") Long companyId);

    /**
     * 通过用户id和公司id更新状态
     * @param status
     * @param userId
     * @param companyId
     * @return
     */
    Integer updateStatusByUserIdAndCompanyId(@Param("status") Integer status,@Param("userId") Long userId, @Param("companyId") Long companyId);
}
