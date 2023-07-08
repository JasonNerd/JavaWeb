package com.rainbow.pojo;

public class User {
    private Integer id;
    private String name;
    private short age;
    private short gender;
    private String phone;

    public User(Integer id, String name, short age, short gender, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public void setGender(short gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public short getAge() {
        return age;
    }

    public short getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public User() {
    }
}
