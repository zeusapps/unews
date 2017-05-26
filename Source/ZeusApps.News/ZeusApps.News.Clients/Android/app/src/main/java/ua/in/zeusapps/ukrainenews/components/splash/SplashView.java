package ua.in.zeusapps.ukrainenews.components.splash;

import com.arellomobile.mvp.MvpView;

interface SplashView extends MvpView {
    void showLoading();
    void showChecking();
    void showError();
}
