package com.example.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    //private String name;
    //private Integer age;
    private String username;
    private String password;
    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //public String getName() {
    //    return name;
    //}

    //public void setName(String name) {
    //    this.name = name;
    //}

    //public Integer getAge() {
    //    return age;
    //}

    //public void setAge(Integer age) {
    //    this.age = age;
    //}

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
