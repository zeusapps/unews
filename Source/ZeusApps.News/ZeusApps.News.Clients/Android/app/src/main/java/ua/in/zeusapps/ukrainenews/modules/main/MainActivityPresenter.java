package ua.in.zeusapps.ukrainenews.modules.main;


import java.util.List;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.models.Source;

public class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;
    private MainActivityMVP.Model _model;

    public MainActivityPresenter(MainActivityMVP.Model model) {
        _model = model;
    }

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;

        _model.getSources().subscribe(new Subscriber<List<Source>>() {
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
