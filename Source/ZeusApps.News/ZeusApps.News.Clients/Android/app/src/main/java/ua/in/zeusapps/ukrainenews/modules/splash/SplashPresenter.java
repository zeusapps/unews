package ua.in.zeusapps.ukrainenews.modules.splash;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SplashPresenter implements SplashMVP.IPresenter {
    private SplashMVP.IView _view;
    private SplashMVP.IModel _model;

    public SplashPresenter(
            SplashMVP.IModel model){
        _model = model;
    }

    @Override
    public Observable<Boolean> prepare() {
        return _model
                .ensureSources()
                .map(new Func1<List<Source>, Boolean>() {
                    @Override
                    public Boolean call(List<Source> sources) {
                        return sources != null && sources.size() > 0;
                    }
                });
    }

    @Override
    public void setView(SplashMVP.IView view) {
        _view = view;
    }
}