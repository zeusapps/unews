package ua.in.zeusapps.ukrainenews.modules.settings;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.modules.root.RootRouter;

@InjectViewState
public class SettingsPresenter
        extends MvpPresenterBase<SettingsView, RootRouter> {

    @Inject
    IArticleRepository repository;

    public SettingsPresenter(){
        getComponent().inject(this);
    }

    void clearStorage() {
        repository.removeAll();
        getViewState().articlesCleared();
        getRouter().showSources();
    }
}
