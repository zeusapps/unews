package ua.in.zeusapps.ukrainenews.components;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewFragment;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewModule;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleModule;
import ua.in.zeusapps.ukrainenews.modules.main.MainActivity;
import ua.in.zeusapps.ukrainenews.modules.main.MainModule;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsActivity;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsModule;
import ua.in.zeusapps.ukrainenews.modules.splash.SplashActivity;
import ua.in.zeusapps.ukrainenews.modules.splash.SplashModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        MainModule.class,
        ArticleModule.class,
        ArticleViewModule.class,
        SettingsModule.class,
        SplashModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity target);
    void inject(ArticleFragment fragment);
    void inject(ArticleViewFragment fragment);
    void inject(SettingsActivity settingsActivity);
    void inject(SplashActivity splashActivity);
}
