package com.example.demo1.service;

import com.example.demo1.entity.UserInfo;
import com.example.demo1.common.Result;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.vo.UserDetailVO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
    Result<Object> getUserPage(Integer pageNum,Integer pageSize);

    Result<String> updateUserInfo(UserInfo userInfo);

    Result<UserDetailVO> getUserDetail(Long userId);

    Result<String> deleteUserInfo(Long userId);
}
