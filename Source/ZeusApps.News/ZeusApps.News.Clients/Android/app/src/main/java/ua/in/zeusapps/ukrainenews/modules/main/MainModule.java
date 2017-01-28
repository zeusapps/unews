package ua.in.zeusapps.ukrainenews.modules.main;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewMVP;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleMVP;

@Module
public class MainModule {

    @Provides
    public MainActivityMVP.Presenter provideMainActivityPresenter(
            ArticleMVP.IPresenter articlePresenter,
            ArticleViewMVP.IPresenter articleViewPresenter){
        return new MainActivityPresenter(articlePresenter, articleViewPresenter);
    }
}
