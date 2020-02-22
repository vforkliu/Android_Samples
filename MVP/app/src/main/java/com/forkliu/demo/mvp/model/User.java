package com.forkliu.demo.mvp.model;

public class User implements IUser {
    private UserBean bean;

    @Override
    public void SaveUserInfo(UserBean user) {
        this.bean = user;
    }

    @Override
    public UserBean LoadUserInfo() {
        if (bean != null){
            return bean;
        }
        return null;
    }
}
