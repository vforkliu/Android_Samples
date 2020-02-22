package com.forkliu.demo.mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forkliu.demo.mvp.model.UserBean;
import com.forkliu.demo.mvp.presenter.UserPresenter;
import com.forkliu.demo.mvp.view.IUserView;

public class MainActivity extends AppCompatActivity implements IUserView {

    private EditText edt_id;
    private EditText edt_name;
    private TextView tv_show;
    private Button btn_load;
    private Button btn_save;
    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initEvent() {
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadUser();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveUser();
                Toast.makeText(MainActivity.this, "保存完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public UserBean getUser() {
        int id = Integer.valueOf(edt_id.getText().toString().trim());
        String name = edt_name.getText().toString().trim();
        if (name != null) {
            UserBean bean = new UserBean();
            bean.setId(id);
            bean.setName(name);
            return bean;
        }
        return null;
    }

    @Override
    public void setUser(UserBean bean) {
        if (bean != null) {
            tv_show.setText(bean.getId() + "\n" + bean.getName());
        }
    }
}
