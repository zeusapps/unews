package ua.in.zeusapps.ukrainenews.components.main;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView, MainRouter> {
    @Inject
    MainRouter router;

    public MainPresenter() {
        getComponent().inject(this);
        getViewState().showHello();
    }

    @Override
    protected void onFirstViewAttach() {
        //getRouter().showSources();
    }

    void showSettings(){
        getRouter().showSettings();
    }

    void init(){
        getViewState().init();
    }

    void pageChanged(int position){
        getViewState().changePage(position);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }
}
