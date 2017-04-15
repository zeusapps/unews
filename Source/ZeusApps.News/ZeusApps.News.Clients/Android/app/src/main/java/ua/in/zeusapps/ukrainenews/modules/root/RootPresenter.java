package ua.in.zeusapps.ukrainenews.modules.root;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class RootPresenter extends MvpPresenter<RootView> {
    public RootPresenter() {
        getViewState().showMessage("Hello, Moxy!");
    }

    @Override
    protected void onFirstViewAttach() {
        getViewState().showSources();
    }
}
