package com.example.demo1.mapper;

import com.example.demo1.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo1.vo.UserDetailVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Select("""
        SELECT
            u.id AS userId,
            u.username,
            i.real_name AS realName,
            i.phone,
            i.address
        FROM sys_user u
        LEFT JOIN user_info i ON u.id = i.user_id
        WHERE u.id = #{userId}
        """)
    UserDetailVO getUserDetail(@Param("userId") Long userId);

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    @Update("""
        UPDATE user_info
        SET real_name = #{realName},
            phone = #{phone},
            sex = #{sex}
        WHERE user_id = #{userId}
        """)
    int updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户信息
     * @param userId
     * @return
     */
    @Delete("DELETE FROM user_info WHERE user_id = #{userId}")
    int deleteUserInfo(@Param("userId") Long userId);

}
