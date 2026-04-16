package com.example.demo1.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo1.common.Result;
import com.example.demo1.common.ResultCode;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.entity.User;
import com.example.demo1.mapper.UserInfoMapper;
import com.example.demo1.mapper.UserMapper;
import com.example.demo1.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.example.demo1.entity.UserInfo;


@Service
public class UserServiceImpl implements UserService {

    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;


    /**
     * 注册账号
     * @param userDTO
     * @return
     */
    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 查询该用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        if (dbUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        // 2. 组装实体对象
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        // 3. 插入数据库
        userMapper.insert(user);

        return Result.success("注册成功！");
    }

    /**
     * 登录账号
     * @param userDTO
     * @return
     */
    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 根据用户名查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        // 2. 校验用户是否存在
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        // 3. 校验密码
        if (!dbUser.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        return Result.success("登录成功！");
    }

    /**
     * 用id查询账号
     * @param id
     * @return
     */
    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("查询用户成功");
    }

    /**
     * 分页查询用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象 (参数1：当前页码，参数2：每页显示条数)
        Page<User> pageParam = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询 (参数1：分页对象，参数2：查询条件 Wrapper，这里传 null 代表
        // 框架会自动执行一条 COUNT 语句查总数，再拼接 LIMIT 执行分页
        Page<User> resultPage = userMapper.selectPage(pageParam, null);

        // 3. 返回结果 (resultPage 中包含了 records 数据列表、total 总条数、pages 总页
        return Result.success(resultPage);
    }

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先查缓存
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isBlank()) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(cacheVO);
            } catch (Exception e) {
                // 缓存数据异常，删掉脏缓存，继续查数据库
                redisTemplate.delete(key);
            }
        }

        // 2. 查数据库
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 3. 写缓存
        redisTemplate.opsForValue().set(
                key,
                JSONUtil.toJsonStr(detail),
                10,
                TimeUnit.MINUTES
        );

        return Result.success(detail);
    }

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserInfo(UserInfo userInfo) {
        // 参数校验（和你图片中的逻辑一致）
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.error("参数错误：用户ID不能为空");
        }

        Long userId = userInfo.getUserId();
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先更新数据库
        int rows = userInfoMapper.updateUserInfo(userInfo);
        if (rows <= 0) {
            return Result.error("用户信息更新失败");
        }

        // 2. 删除缓存（写操作后删除缓存，下次查询自动重建）
        redisTemplate.delete(key);

        return Result.success("用户信息更新成功");
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteUserInfo(Long userId) {
        // 参数校验
        if (userId == null) {
            return Result.error("参数错误：用户ID不能为空");
        }

        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先删除数据库数据
        int rows = userInfoMapper.deleteUserInfo(userId);
        if (rows <= 0) {
            return Result.error("用户不存在或删除失败");
        }

        // 2. 删除缓存
        redisTemplate.delete(key);

        return Result.success("用户删除成功");
    }
}