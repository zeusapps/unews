package ua.in.zeusapps.ukrainenews.components;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.zeusapps.ukrainenews.components.details.DetailsPresenter;
import ua.in.zeusapps.ukrainenews.components.details.fragments.ArticleDetailsFragment;
import ua.in.zeusapps.ukrainenews.components.details.fragments.ArticleDetailsPresenter;
import ua.in.zeusapps.ukrainenews.data.DataModule;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;
import ua.in.zeusapps.ukrainenews.modules.articlesDetails.ArticleViewFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.articles.ArticlePresenter;
import ua.in.zeusapps.ukrainenews.modules.articlesDetails.ArticleViewPresenter;
import ua.in.zeusapps.ukrainenews.modules.articlesDetails.ArticlesDetailsPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainPresenter;
import ua.in.zeusapps.ukrainenews.components.main.fragments.settings.SettingsPresenter;
import ua.in.zeusapps.ukrainenews.components.main.fragments.sources.SourcesPresenter;
import ua.in.zeusapps.ukrainenews.components.splash.SplashPresenter;


@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class
})
public interface ApplicationComponent {
    void inject(ArticleFragment fragment);
    void inject(ArticleViewFragment fragment);
    void inject(SourcesPresenter presenter);
    void inject(SplashPresenter presenter);
    void inject(ArticlePresenter presenter);
    void inject(SettingsPresenter presenter);
    void inject(MainPresenter presenter);
    void inject(ArticleViewPresenter presenter);
    void inject(ArticlesDetailsPresenter presenter);
    void inject(DetailsPresenter presenter);
    void inject(ArticleDetailsPresenter presenter);
    void inject(ArticleDetailsFragment fragment);
}
