package ua.in.zeusapps.ukrainenews.modules.splash;

import com.arellomobile.mvp.MvpView;

interface SplashView extends MvpView {
    void showLoading();
    void showChecking();
    void showError();
    void startApp();
}
