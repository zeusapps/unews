package ua.in.zeusapps.ukrainenews.modules.settings;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.components.root.RootRouter;

@InjectViewState
public class SettingsPresenter
        extends MvpPresenter<SettingsView, RootRouter> {

    @Inject
    IArticleRepository repository;
    @Inject
    RootRouter router;

    SettingsPresenter(){
        getComponent().inject(this);
    }

    @Override
    public RootRouter getRouter() {
        return router;
    }

    void clearStorage() {
        repository.removeAll();
        getViewState().articlesCleared();
        getRouter().showSources();
    }
}
