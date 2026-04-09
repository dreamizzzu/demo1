package com.example.demo1.controller;

import com.example.demo1.common.Result;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.entity.User;
import com.example.demo1.service.UserService;
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

    /*@PutMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return "更新成功，ID " + id + " 的用户已修改为: " + user.getName();
    }*/

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        return "删除成功，已移除 ID 为 " + id + " 的用户";
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
}
