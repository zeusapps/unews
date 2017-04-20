package ua.in.zeusapps.ukrainenews.components;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.zeusapps.ukrainenews.data.DataModule;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;
import ua.in.zeusapps.ukrainenews.modules.articleDetails.ArticleDetailsPresenter;
import ua.in.zeusapps.ukrainenews.modules.articleDetails.ArticleDetailsFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticlePresenter;
import ua.in.zeusapps.ukrainenews.modules.main.MainActivity;
import ua.in.zeusapps.ukrainenews.modules.main.MainModule;
import ua.in.zeusapps.ukrainenews.modules.root.RootPresenter;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsPresenter;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesPresenter;
import ua.in.zeusapps.ukrainenews.modules.splash.SplashPresenter;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        MainModule.class,
        DataModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity target);
    void inject(ArticleFragment fragment);
    void inject(ArticleDetailsFragment fragment);

    void inject(SourcesPresenter presenter);
    void inject(SplashPresenter presenter);
    void inject(ArticlePresenter presenter);
    void inject(SettingsPresenter presenter);
    void inject(RootPresenter presenter);
    void inject(ArticleDetailsPresenter presenter);
}
