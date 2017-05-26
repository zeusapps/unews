package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalSourcesInteractor;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.root.RootRouter;

@InjectViewState
public class SourcesPresenter extends MvpPresenter<SourcesView, RootRouter> {
    @Inject
    GetLocalSourcesInteractor interactor;
    @Inject
    RootRouter router;

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
    public RootRouter getRouter() {
        return router;
    }

    void showArticles(Source source){
        getRouter().showArticles(source);
    }
}