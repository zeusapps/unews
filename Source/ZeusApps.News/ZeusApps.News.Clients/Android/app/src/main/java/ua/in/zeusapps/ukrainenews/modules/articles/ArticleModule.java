package ua.in.zeusapps.ukrainenews.modules.articles;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ArticleModule {
    @Provides
    @Singleton
    public ArticleMVP.IPresenter providePresenter(ArticleMVP.IModel model){
        return new ArticlePresenter(model);
    }

    @Provides
    @Singleton
    public ArticleMVP.IModel provideModel(){
        return new ArticleModel();
    }
}
