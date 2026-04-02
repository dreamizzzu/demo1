package com.example.demo1.service;

import com.example.demo1.common.Result;
import com.example.demo1.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
}
