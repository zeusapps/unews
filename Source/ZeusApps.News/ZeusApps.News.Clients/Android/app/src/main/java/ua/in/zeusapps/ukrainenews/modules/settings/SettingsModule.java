package ua.in.zeusapps.ukrainenews.modules.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.IRepository;

@Module
public class SettingsModule {
    @Provides
    @Singleton
    public SettingsMVP.Presenter providesSettingsPresenter(IRepository repository){
        return new SettingsPresenter(repository);
    }
}