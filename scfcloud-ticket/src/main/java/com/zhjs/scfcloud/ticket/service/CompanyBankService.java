package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhjs.scfcloud.model.dto.ticket.PermissionBankListDTO;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;

public interface CompanyBankService {
    /**
     * 运营管理-查看当前登录用户权限内的公司银行列表
     * @param userId
     * @param permissionBankListDTO
     * @return
     */
    IPage<CompanyBankAccount> findCompanyBanksByUserPermission(Long userId, PermissionBankListDTO permissionBankListDTO);
}
