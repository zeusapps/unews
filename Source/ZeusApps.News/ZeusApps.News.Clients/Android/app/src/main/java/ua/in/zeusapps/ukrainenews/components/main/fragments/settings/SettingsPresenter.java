package ua.in.zeusapps.ukrainenews.components.main.fragments.settings;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.services.SettingsService;

@InjectViewState
public class SettingsPresenter
        extends MvpPresenter<SettingsView, MainRouter> {

    @Inject
    IArticleRepository repository;
    @Inject
    MainRouter router;
    @Inject
    SettingsService settingsService;

    SettingsPresenter(){
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    void init(){
        getViewState().setArticleTemplateType(settingsService.getArticleTemplateType());
    }

    void clearStorage() {
        repository.removeAll();
        getViewState().articlesCleared();
        //getRouter().showSources();
    }

    void setArticleItemTemplateType(int articleItemTemplateType){
        settingsService.setArticleTemplateType(articleItemTemplateType);
    }
}
