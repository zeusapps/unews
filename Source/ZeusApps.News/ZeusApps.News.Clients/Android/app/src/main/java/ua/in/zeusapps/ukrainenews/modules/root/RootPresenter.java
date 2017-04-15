package ua.in.zeusapps.ukrainenews.modules.root;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class RootPresenter extends MvpPresenter<RootView> {
    private static RootPresenter _instance;

    public RootPresenter() {
        _instance = this;
        getViewState().showMessage("Hello, Moxy!");
    }

    @Override
    protected void onFirstViewAttach() {
        showSources();
    }

    public void showSources(){
        getViewState().showSources();
    }

    public void showArticles(Source source){
        getViewState().showArticles(source);
    }

    //TODO move to router
    public static RootPresenter getInstance(){
        return _instance;
    }
}
