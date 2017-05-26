package ua.in.zeusapps.ukrainenews.components.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
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
        ensureSourcesInteractor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showError(e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getRouter().startApp();
            }
        });
    }

    @Override
    public SplashRouter getRouter() {
        return router;
    }

    private void showError(Throwable error){
        Log.e(SplashPresenter.class.getSimpleName(), error.getMessage(), error);
        getViewState().showError();
        Observable.interval(4000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.exit(0);
                    }
                });
    }

}