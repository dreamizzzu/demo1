package com.example.demo1.entity;

public class User {
    private Long id;
    private String name;
    private Integer age;

    // 必须添加无参构造器（Spring 解析 JSON 时需要）
    public User() {}

    // getter/setter 方法（必须，否则无法获取/设置字段值）
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
