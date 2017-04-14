package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.domain.GetLocalSourcesInteractor;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class SourcesPresenter extends MvpPresenterBase<SourcesView> {

    @Inject
    GetLocalSourcesInteractor interactor;

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

    SourcesPresenter() {
        getComponent().inject(this);
    }
}