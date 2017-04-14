package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class SourcesPresenter extends MvpPresenterBase<SourcesView> {

    @Inject
    SourcesModel model;

    @Override
    protected void onFirstViewAttach() {
        List<Source> sources = model.getSources();
        getViewState().showSources(sources);
    }

    public SourcesPresenter() {
        getComponent().inject(this);
        getViewState().showMessage("Hello World!");
    }
}