package ua.in.zeusapps.ukrainenews.common;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class MvpPresenterBase<TView extends MvpView, TRouter extends RouterBase>
        extends MvpPresenter<TView> {

    private TRouter _router;

    public void setRouter(TRouter router){
        _router = router;
    }

    protected TRouter getRouter(){
        return _router;
    }

    protected ApplicationComponent getComponent(){
        return App.getInstance().getComponent();
    }

}
