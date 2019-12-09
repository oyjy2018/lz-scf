package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Comment;
import com.zhjs.scfcloud.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-22 16:45
 */
public interface CommentMapper extends BaseMapper<Comment> {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentVO> findList(@Param("companyId") Long companyId, @Param("businessId") Long businessId,
                             @Param("businessTypeId") Long businessTypeId, @Param("flowCode") String flowCode);
}