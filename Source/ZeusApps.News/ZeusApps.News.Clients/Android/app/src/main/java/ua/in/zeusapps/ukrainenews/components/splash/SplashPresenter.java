package ua.in.zeusapps.ukrainenews.components.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.InitialLoadInteractor;
import ua.in.zeusapps.ukrainenews.models.SourceBundle;

@InjectViewState
public class SplashPresenter extends MvpPresenter<SplashView, SplashRouter> {

    @Inject
    InitialLoadInteractor _initialLoadInteractor;
    @Inject
    SplashRouter router;

    SplashPresenter() {
        getComponent().inject(this);
        getViewState().showLoading();
    }

    @Override
    protected void onFirstViewAttach() {
        _initialLoadInteractor.execute(new Observer<SourceBundle>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                _initialLoadInteractor.register(d);
            }

            @Override
            public void onNext(@NonNull SourceBundle sourceBundle) {
                getViewState().showLoadingSource(sourceBundle.getSource());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                showError(e);
            }

            @Override
            public void onComplete() {
                startApp();
            }
        }, null);
    }

    @Override
    public SplashRouter getRouter() {
        return router;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _initialLoadInteractor.unsubscribe();
    }

    private void showError(Throwable error){
        Log.e(SplashPresenter.class.getSimpleName(), error.getMessage(), error);
        getViewState().showError();
        Observable
                .interval(4000, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.exit(0));
    }

    private void startApp(){
        getRouter().startApp();
    }
}