package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.articles.AdsProvider;

@Module
public class DataModule {

    public DataModule() {
        RepositoryBase.RegisterClass(Article.class);
        RepositoryBase.RegisterClass(Source.class);
        RepositoryBase.setDatabaseName("news.db");
        RepositoryBase.setDatebaseVersion(10);
    }

    @Provides
    @Singleton
    ISourceRepository providesSourcesRepository(Context context){
        return new SourceRepository(context);
    }

    @Provides
    @Singleton
    IArticleRepository providesArticlesRepository(Context context){
        return new ArticleRepository(context);
    }

    @Provides
    @Singleton
    RecyclerViewAdapter.AdsProvider providesAdsProvider(Context context){
        return new AdsProvider(context);
    }
}
