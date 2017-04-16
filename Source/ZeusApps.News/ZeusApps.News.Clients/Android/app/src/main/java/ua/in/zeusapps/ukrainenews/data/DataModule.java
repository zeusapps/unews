package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.modules.articles.AdsProvider;

@Module
public class DataModule {

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
