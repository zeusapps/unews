package ua.in.zeusapps.ukrainenews.common;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public abstract class MvpPresenterBase<T extends MvpView> extends MvpPresenter<T> {

    protected ApplicationComponent getComponent(){
        return App.getInstance().getComponent();
    }

}
