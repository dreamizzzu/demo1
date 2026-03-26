package com.example.demo1.service;

import com.example.demo1.common.Result;
import com.example.demo1.common.ResultCode;
import com.example.demo1.dto.UserDTO;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service // 必须添加该注解，将业务类交给 Spring 容器管理
public class UserServiceImpl implements UserService {

    // 暂时使用 Map 模拟数据库。下节课将在这里 @Autowired 注入 UserMapper
    private static final Map<String, String> userDb = new HashMap<>();

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 校验用户是否已存在
        if (userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        // 2. 存入模拟数据库
        userDb.put(userDTO.getUsername(), userDTO.getPassword());

        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 校验用户是否存在
        if (!userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 校验密码是否正确
        String dbPassword = userDb.get(userDTO.getUsername());
        if (!dbPassword.equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        // 这里省略了登录成功后的 token 生成逻辑
        return Result.success("登录成功");
    }
}