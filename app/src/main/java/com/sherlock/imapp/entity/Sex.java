package com.sherlock.imapp.entity;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class Sex {
    private int sex;
    private String name;

    public Sex(int sex, String name) {
        this.sex = sex;
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
