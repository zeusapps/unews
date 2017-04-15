package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    @Singleton
    public ISourceRepository providesSourcesRepository(Context context){
        return new SourceRepository(context);
    }

    @Provides
    @Singleton
    public IArticleRepository providesArticlesRepository(Context context){
        return new ArticleRepository(context);
    }
}
