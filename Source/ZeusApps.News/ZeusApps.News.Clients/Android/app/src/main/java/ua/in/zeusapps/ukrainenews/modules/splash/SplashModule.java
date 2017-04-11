package ua.in.zeusapps.ukrainenews.modules.splash;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.IRepository;
import ua.in.zeusapps.ukrainenews.services.IDataService;

@Module
public class SplashModule {

    @Provides
    @Singleton
    public SplashMVP.IPresenter providesPresenter(SplashMVP.IModel model){
        return new SplashPresenter(model);
    }

    @Provides
    @Singleton
    public SplashMVP.IModel providesModel(
            IRepository repository,
            IDataService dataService){
        return new SplashModel(repository, dataService);
    }
}
