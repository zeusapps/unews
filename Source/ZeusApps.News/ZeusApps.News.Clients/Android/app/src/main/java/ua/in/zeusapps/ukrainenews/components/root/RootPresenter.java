package ua.in.zeusapps.ukrainenews.components.root;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;

@InjectViewState
public class RootPresenter extends MvpPresenter<RootView, RootRouter> {
    @Inject
    RootRouter router;

    public RootPresenter() {
        getComponent().inject(this);
        getViewState().showHello();
    }

    @Override
    protected void onFirstViewAttach() {
        getRouter().showSources();
    }

    void showSettings(){
        getRouter().showSettings();
    }

    @Override
    public RootRouter getRouter() {
        return router;
    }
}
