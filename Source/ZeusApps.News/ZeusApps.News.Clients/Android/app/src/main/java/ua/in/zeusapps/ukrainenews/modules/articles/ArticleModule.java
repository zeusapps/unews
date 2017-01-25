package ua.in.zeusapps.ukrainenews.modules.articles;

import dagger.Module;
import dagger.Provides;

@Module
public class ArticleModule {
    @Provides
    public ArticleMVP.IPresenter providePresenter(ArticleMVP.IModel model){
        return new ArticlePresenter(model);
    }

    @Provides
    public ArticleMVP.IModel provideModel(){
        return new ArticleModel();
    }
}
