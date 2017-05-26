package ua.in.zeusapps.ukrainenews.components.main.fragments.settings;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;

@InjectViewState
public class SettingsPresenter
        extends MvpPresenter<SettingsView, MainRouter> {

    @Inject
    IArticleRepository repository;
    @Inject
    MainRouter router;

    SettingsPresenter(){
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    void clearStorage() {
        repository.removeAll();
        getViewState().articlesCleared();
        getRouter().showSources();
    }
}
