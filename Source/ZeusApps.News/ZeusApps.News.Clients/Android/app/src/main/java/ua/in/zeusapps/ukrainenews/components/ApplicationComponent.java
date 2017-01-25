package ua.in.zeusapps.ukrainenews.components;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleModule;
import ua.in.zeusapps.ukrainenews.modules.main.MainActivity;
import ua.in.zeusapps.ukrainenews.modules.main.MainModule;
import ua.in.zeusapps.ukrainenews.modules.source.SourceFragment;
import ua.in.zeusapps.ukrainenews.modules.source.SourceModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        MainModule.class,
        SourceModule.class,
        ArticleModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity target);
    void inject(SourceFragment fragment);
    void inject(ArticleFragment fragment);
}
