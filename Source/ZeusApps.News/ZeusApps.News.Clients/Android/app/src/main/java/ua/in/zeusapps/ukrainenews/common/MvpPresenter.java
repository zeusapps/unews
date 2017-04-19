package ua.in.zeusapps.ukrainenews.common;

import com.arellomobile.mvp.MvpView;

import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class MvpPresenter<TView extends MvpView, TRouter extends MvpRouter>
        extends com.arellomobile.mvp.MvpPresenter<TView> {

    public abstract TRouter getRouter();

    protected ApplicationComponent getComponent(){
        return App.getInstance().getComponent();
    }

}
