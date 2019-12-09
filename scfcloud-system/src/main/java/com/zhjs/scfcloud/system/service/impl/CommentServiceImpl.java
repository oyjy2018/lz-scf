package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.Comment;
import com.zhjs.scfcloud.model.mapper.CommentMapper;
import com.zhjs.scfcloud.model.vo.CommentVO;
import com.zhjs.scfcloud.system.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-21 15:46
 *
 * @author liuchanghai
 * @create 2019-06-21 15:46
 * @since
 */

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    /**
     * 查询评论
     * @param companyId 公司ID
     * @param businessId 业务ID
     * @param businessTypeId 业务类型ID
     * @return
     */
    @Override
    public List<CommentVO> findList(Long companyId, Long businessId, Long businessTypeId, String flowCode) {
        if(companyId == null && businessId == null && businessTypeId == null){
            return null;
        }

        return this.baseMapper.findList(companyId, businessId, businessTypeId, flowCode);
    }
}
