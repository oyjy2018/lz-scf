package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.RoleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/7/5 13:48
 */
@FeignClient(value = "scfcloud-system", contextId = "role")
public interface RoleFeignService {
    /**
     * 查询用户在指定公司下的角色
     * @param companyId
     * @param userId
     * @return
     */
    @PostMapping("/role/findRoleListByCompanyIdAndUserId")
    Result<List<Role>> findRoleListByCompanyIdAndUserId(@RequestParam Long companyId, @RequestParam Long userId);
}
