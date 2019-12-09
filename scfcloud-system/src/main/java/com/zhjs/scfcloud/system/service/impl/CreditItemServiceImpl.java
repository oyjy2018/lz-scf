package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.CreditItem;
import com.zhjs.scfcloud.model.mapper.CreditItemMapper;
import com.zhjs.scfcloud.system.service.CreditItemService;
import org.springframework.stereotype.Service;

@Service
public class CreditItemServiceImpl extends ServiceImpl<CreditItemMapper, CreditItem> implements CreditItemService {
}
