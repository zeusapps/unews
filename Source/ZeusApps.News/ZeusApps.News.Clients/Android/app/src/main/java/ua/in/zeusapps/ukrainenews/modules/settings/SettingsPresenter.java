package ua.in.zeusapps.ukrainenews.modules.settings;

import ua.in.zeusapps.ukrainenews.services.IRepository;

public class SettingsPresenter implements SettingsMVP.Presenter{

    private SettingsMVP.View _view;
    private IRepository _repository;

    public SettingsPresenter(IRepository repository){
        _repository = repository;
    }

    @Override
    public void clearStorage() {
        _repository.deleteAlArticles();
    }

    @Override
    public void setView(SettingsMVP.View view) {
        _view = view;
    }
}
