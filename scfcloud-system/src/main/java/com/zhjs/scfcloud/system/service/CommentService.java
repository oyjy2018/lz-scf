package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.Comment;
import com.zhjs.scfcloud.model.vo.CommentVO;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-21 15:45
 *
 * @author liuchanghai
 * @create 2019-06-21 15:45
 * @since
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查询评论
     * @param companyId 公司ID
     * @param businessId 业务ID
     * @param businessTypeId 业务类型ID
     * @return
     */
    List<CommentVO> findList(Long companyId, Long businessId, Long businessTypeId, String flowCode);
}
