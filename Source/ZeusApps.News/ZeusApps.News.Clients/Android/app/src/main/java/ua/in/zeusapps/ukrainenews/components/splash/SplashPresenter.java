package ua.in.zeusapps.ukrainenews.components.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.EnsureSourcesInteractor;

@InjectViewState
public class SplashPresenter extends MvpPresenter<SplashView, SplashRouter> {

    @Inject
    EnsureSourcesInteractor ensureSourcesInteractor;
    @Inject
    SplashRouter router;

    SplashPresenter() {
        getComponent().inject(this);
        getViewState().showLoading();
    }

    @Override
    protected void onFirstViewAttach() {
        ensureSourcesInteractor.executeWithError(
                this::startApp,
                this::showError);
    }

    @Override
    public SplashRouter getRouter() {
        return router;
    }

    private void showError(Throwable error){
        Log.e(SplashPresenter.class.getSimpleName(), error.getMessage(), error);
        getViewState().showError();
        Observable
                .interval(4000, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.exit(0));
    }

    private void startApp(Boolean value){
        getRouter().startApp();
    }
}