package ua.in.zeusapps.ukrainenews.components.main.base;

import com.arellomobile.mvp.MvpView;

public interface BaseMainView extends MvpView{
    void showError(String message);
    void showInfo(String message);
}
