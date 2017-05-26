package ua.in.zeusapps.ukrainenews.components.main.fragments.sources;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
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
        interactor.execute(new Subscriber<List<Source>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getViewState().showError();
            }

            @Override
            public void onNext(List<Source> sources) {
                getViewState().showSources(sources);
            }
        });
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    void showArticles(Source source){
        getRouter().showArticles(source);
    }
}