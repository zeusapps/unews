package ua.in.zeusapps.ukrainenews.modules.splash;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.domain.EnsureSourcesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetSourcesInteractor;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class SplashPresenter extends MvpPresenterBase<SplashView> {

    @Inject
    GetSourcesInteractor sourcesInteractor;
    @Inject
    EnsureSourcesInteractor ensureSourcesInteractor;

    public SplashPresenter() {
        getComponent().inject(this);
        getViewState().showLoading();
    }

    @Override
    protected void onFirstViewAttach() {
        // TODO concat getting new sources and check for existence
        sourcesInteractor.execute(new Subscriber<List<Source>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showError(e);
            }

            @Override
            public void onNext(List<Source> sources) {
                ensureSourcesInteractor.execute(sources, new Subscriber<Boolean>() {
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
        });
    }

    private void showError(Throwable error){
        // TODO Show error
        Log.e(SplashPresenter.class.getSimpleName(), error.getMessage(), error);

        System.exit(0);

    }
}