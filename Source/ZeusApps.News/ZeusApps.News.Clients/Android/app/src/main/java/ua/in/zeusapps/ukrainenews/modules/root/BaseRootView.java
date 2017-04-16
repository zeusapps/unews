package ua.in.zeusapps.ukrainenews.modules.root;

import com.arellomobile.mvp.MvpView;

public interface BaseRootView extends MvpView{
    void showError(String message);
    void showInfo(String message);
}
