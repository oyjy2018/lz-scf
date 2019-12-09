package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.RoleUserVO;
import com.zhjs.scfcloud.model.vo.UserListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户账号表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-20 11:17
 */
public interface UserMapper extends BaseMapper<User> {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据入参选择性 分页查询成员管理列表
     * @param page 分页参数
     * @param companyId 公司ID
     * @param deptId 部门ID
     * @param userIDs 用户ID集合
     * @param status 成员状态
     * @param userName 成员名称
     * @return
     */
    List<UserListVO> querySelective(Page<UserListVO> page, @Param("companyId") Long companyId,
                                    @Param("deptIds") List<Long> deptIds, @Param("userIDs") List<Long> userIDs,
                                    @Param("status") List<Integer> status, @Param("userName") String userName);

    List<RoleUserVO> findByCompanyId(@Param("companyId") Long companyId);

}
