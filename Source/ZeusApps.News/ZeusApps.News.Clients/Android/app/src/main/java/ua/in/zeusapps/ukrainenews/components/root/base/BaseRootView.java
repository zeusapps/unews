package ua.in.zeusapps.ukrainenews.components.root.base;

import com.arellomobile.mvp.MvpView;

public interface BaseRootView extends MvpView{
    void showError(String message);
    void showInfo(String message);
}
