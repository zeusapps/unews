package ua.in.zeusapps.ukrainenews.modules.articleView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.IRepository;

@Module
public class ArticleViewModule {

    @Provides
    @Singleton
    public ArticleViewMVP.IPresenter providesPresenter(IRepository repository){
        return new ArticleViewPresenter(repository);
    }

}