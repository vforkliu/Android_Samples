package com.forkliu.demo.mvp.view;

import com.forkliu.demo.mvp.model.UserBean;

public interface IUserView {
    UserBean getUser();
    void setUser(UserBean bean);
}
