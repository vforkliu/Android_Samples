package com.forkliu.demo.mvp.presenter;

import com.forkliu.demo.mvp.model.IUser;
import com.forkliu.demo.mvp.model.User;
import com.forkliu.demo.mvp.view.IUserView;

public class UserPresenter {
    private IUserView userView;
    private IUser user;

    public UserPresenter(IUserView view) {
        this.userView = view;
        user = new User();
    }

    public void saveUser() {
        user.SaveUserInfo(userView.getUser());
    }

    public void loadUser() {
        userView.setUser(user.LoadUserInfo());;
    }
}
