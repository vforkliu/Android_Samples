package com.forkliu.demo.mvp.model;

public interface IUser {
    void SaveUserInfo(UserBean user);
    UserBean LoadUserInfo();
}
