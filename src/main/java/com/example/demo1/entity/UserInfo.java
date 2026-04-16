package com.example.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info")
public class UserInfo {

    @TableId(type = IdType.AUTO)
    private Long id;      // 用于 updateById

    private Long userId;  // 用于 getUserId()

    // 其他字段根据你的 UserDetailVO 需要的字段来补充
}