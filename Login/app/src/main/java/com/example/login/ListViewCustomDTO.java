package com.example.login;


import java.io.Serializable;

public class ListViewCustomDTO implements Serializable {
    private int resId;
    private String Name;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

}