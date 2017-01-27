package ua.in.zeusapps.ukrainenews.modules.articleView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ArticleViewModule {

    @Provides
    @Singleton
    public ArticleViewMVP.IPresenter providesPresenter(){
        return new ArticleViewPresenter();
    }

}