package com.example.shangyulin.modularizationtest.Bean;

/**
 * Created by shangyulin on 2018/8/21.
 */

public class Student {

    private int id;
    private String name;


    private int sex;
    private String phone;
    private String email;
    private String address;

    private Student(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.sex = builder.sex;
        this.phone = builder.phone;
        this.email = builder.email;
        this.address = builder.address;
    }

    public static class Builder{
        private int id;
        private String name;

        private int sex;
        private String phone;
        private String email;
        private String address;

        public Builder(int id, String name){
            this.id = id;
            this.name = name;
        }

        public Builder phone(String phone){
            this.phone = phone;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder address(String address){
            this.address = address;
            return this;
        }

        public Builder sex(int sex){
            this.sex = sex;
            return this;
        }

        public Student build(){
            return new Student(this);
        }
    }
}
