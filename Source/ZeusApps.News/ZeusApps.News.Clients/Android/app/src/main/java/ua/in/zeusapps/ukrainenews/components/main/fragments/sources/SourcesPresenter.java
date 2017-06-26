package ua.in.zeusapps.ukrainenews.components.main.fragments.sources;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalSourcesInteractor;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class SourcesPresenter extends MvpPresenter<SourcesView, MainRouter> {
    @Inject
    GetLocalSourcesInteractor interactor;
    @Inject
    MainRouter router;

    SourcesPresenter() {
        getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        interactor.execute(
                sources -> getViewState().showSources(sources),
                throwable -> getViewState().showError());
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    void showArticles(Source source){
        getRouter().showArticles(source);
    }
}