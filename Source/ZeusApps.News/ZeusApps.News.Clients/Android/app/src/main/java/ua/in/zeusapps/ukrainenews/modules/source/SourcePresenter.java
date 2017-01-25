package ua.in.zeusapps.ukrainenews.modules.source;

import java.util.List;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourcePresenter implements SourceMVP.IPresenter {

    private SourceMVP.IView _view;
    private final SourceMVP.IModel _model;

    public SourcePresenter(SourceMVP.IModel model) {
        this._model = model;
    }

    @Override
    public void setView(SourceMVP.IView view) {
        _view = view;

        _model
                .getSources()
                .subscribe(new Subscriber<List<Source>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Source> sources) {
                _view.updateSources(sources);
            }
        });
    }
}