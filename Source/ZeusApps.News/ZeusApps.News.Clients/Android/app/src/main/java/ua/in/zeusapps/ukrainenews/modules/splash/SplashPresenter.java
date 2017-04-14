package ua.in.zeusapps.ukrainenews.modules.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.domain.EnsureSourcesInteractor;

@InjectViewState
public class SplashPresenter extends MvpPresenterBase<SplashView> {

    @Inject
    EnsureSourcesInteractor ensureSourcesInteractor;

    public SplashPresenter() {
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
                getViewState().startApp();
            }
        });
    }

    private void showError(Throwable error){
        // TODO Show error
        Log.e(SplashPresenter.class.getSimpleName(), error.getMessage(), error);

        System.exit(0);

    }
}