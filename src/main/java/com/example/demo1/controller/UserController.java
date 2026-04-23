package com.example.demo1.controller;

import com.example.demo1.common.Result;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.entity.User;
import com.example.demo1.entity.UserInfo;
import com.example.demo1.service.UserService;
import com.example.demo1.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新用户注册
     * @param userDTO
     * @return
     */
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO){

        return userService.register(userDTO);
    }

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO){

        return userService.login(userDTO);
    }

    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return  userService.getUserById(id);
    }


    @GetMapping("/test")
    public Result<?> testException() {
        int a = 1 / 0;
        return Result.success("测试成功");
    }

    @GetMapping("/page")
    public Result<Object> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }

    // 5. 查询用户详情（多表联查 + Redis）
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long userId) {
        return userService.getUserDetail(userId);
    }

    // 6. 更新用户扩展信息
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(@PathVariable("id") Long userId,
                                         @RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }

    // 7. 删除用户信息
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUserInfo(userId);
    }
}
