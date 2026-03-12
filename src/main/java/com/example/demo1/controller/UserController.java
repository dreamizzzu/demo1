package com.example.demo1.controller;

import com.example.demo1.common.Result;
import com.example.demo1.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id) {
        return "查询成功，正在返回 ID 为 " + id + " 的用户信息";
    }

    @PostMapping
    public String createUser(@RequestBody User user) {
        return "新增成功，接收到用户：" + user.getName() + ", 年龄: " + user.getAge();
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return "更新成功，ID " + id + " 的用户已修改为: " + user.getName();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        return "删除成功，已移除 ID 为 " + id + " 的用户";
    }

    @GetMapping("/test")
    public Result<?> testException() {
        int a = 1 / 0;
        return Result.success("测试成功");
    }
}
