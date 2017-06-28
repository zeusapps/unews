package ua.in.zeusapps.ukrainenews.components.splash;

import com.arellomobile.mvp.MvpView;

import ua.in.zeusapps.ukrainenews.models.Source;

interface SplashView extends MvpView {
    void showLoading();
    void showLoadingSource(Source source);
    void showChecking();
    void showError();
}
