package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.UserListVO;
import com.zhjs.scfcloud.model.vo.UserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/5/22 11:41
 */

@Mapper(componentModel = "spring")
public interface UserTransfer {

    List<UserListVO> users2UserListVo(List<User> list);

    UserInfoVO toUserInfoVO(User user);

    List<UserVO> users2UserVo(List<User> list);
}
