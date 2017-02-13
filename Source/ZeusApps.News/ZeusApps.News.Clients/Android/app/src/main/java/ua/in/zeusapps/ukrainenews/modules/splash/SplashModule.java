package ua.in.zeusapps.ukrainenews.modules.splash;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ua.in.zeusapps.ukrainenews.modules.articles.IRepository;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

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
            ISourceService sourceService){
        return new SplashModel(repository, sourceService);
    }

    @Provides
    @Singleton
    public ISourceService providesSourceService(Retrofit retrofit){
        return retrofit.create(ISourceService.class);
    }
}
