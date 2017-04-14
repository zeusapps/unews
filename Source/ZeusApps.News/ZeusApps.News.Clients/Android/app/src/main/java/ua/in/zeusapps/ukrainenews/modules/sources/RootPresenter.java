package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class RootPresenter extends MvpPresenter<RootView> {
    public RootPresenter() {
        getViewState().showMessage("Hello, Moxy!");
    }
}
