package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.UserAccount;
import com.zhjs.scfcloud.model.mapper.UserAccountMapper;
import com.zhjs.scfcloud.system.service.UserAccountService;
import org.springframework.stereotype.Service;

/**
 * 用户手机号信息管理业务逻辑实现类
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 14:02
 * @since
 */

@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
}
