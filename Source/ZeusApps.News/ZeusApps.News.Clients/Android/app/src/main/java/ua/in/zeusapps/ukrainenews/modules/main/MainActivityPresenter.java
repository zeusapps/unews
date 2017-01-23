package ua.in.zeusapps.ukrainenews.modules.main;


public class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;
    private MainActivityMVP.Model _model;

    public MainActivityPresenter(MainActivityMVP.Model model) {
        _model = model;
    }

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;
    }
}
