package ua.in.zeusapps.ukrainenews.modules.root;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class RootPresenter extends MvpPresenterBase<RootView, RootRouter> {
    public RootPresenter() {
        getViewState().showMessage("Hello, Moxy!");
    }

    @Override
    protected void onFirstViewAttach() {
        getRouter().showSources();
    }

    void showSettings(){
        getRouter().showSettings();
    }

}
