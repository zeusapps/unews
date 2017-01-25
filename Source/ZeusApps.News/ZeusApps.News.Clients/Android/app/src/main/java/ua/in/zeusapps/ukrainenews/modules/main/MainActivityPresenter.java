package ua.in.zeusapps.ukrainenews.modules.main;

public class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;

    public MainActivityPresenter() {

    }

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;
    }
}
