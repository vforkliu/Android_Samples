package com.forkliu.demo.mvp.presenter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public class BasePresenter implements IPresenter {
    private static final String TAG = "com.forkliu.demo.mvp.presenter.BasePresenter";
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.d("tag", "BasePresenter.onCreate" + this.getClass().toString());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d("tag", "BasePresenter.onDestroy" + this.getClass().toString());
    }

    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {

    }
}
