package ua.in.zeusapps.ukrainenews.components.main.fragments.pager;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;

@InjectViewState
public class PagerPresenter extends MvpPresenter<PagerView, MainRouter> {

    @Inject
    MainRouter _mainRouter;

    public PagerPresenter() {
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return _mainRouter;
    }

    public void init() {
        getViewState().init();
    }

    public void pageChanged(int position) {
        getViewState().changePage(position);
    }
}
