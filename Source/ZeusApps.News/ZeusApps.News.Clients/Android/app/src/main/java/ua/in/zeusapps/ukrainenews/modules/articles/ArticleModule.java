package ua.in.zeusapps.ukrainenews.modules.articles;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.IDataService;
import ua.in.zeusapps.ukrainenews.services.IRepository;

@Module
public class ArticleModule {

    @Provides
    @Singleton
    public ArticleMVP.IPresenter providePresenter(ArticleMVP.IModel model){
        return new ArticlePresenter(model);
    }

    @Provides
    @Singleton
    public ArticleMVP.IModel provideModel(
            IRepository repository,
            IDataService dataService){
        return new ArticleModel(repository, dataService);
    }
}
