package ua.in.zeusapps.ukrainenews.modules.articles;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.IArticleService;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

@Module
public class ArticleModule {

    @Provides
    @Singleton
    public ArticleMVP.IPresenter providePresenter(ArticleMVP.IModel model){
        return new ArticlePresenter(model);
    }

    @Provides
    @Singleton
    public ArticleMVP.IModel provideModel(
            IRepository repository,
            ISourceService sourceService,
            IArticleService articleService){
        return new ArticleModel(repository, articleService);
    }

    @Provides
    @Singleton
    public IRepository provideRepository(Context context, Formatter formatter){
        return new Repository(context, formatter);
    }

    @Provides
    @Singleton
    public IArticleService providesArticleService(Retrofit retrofit){
        return retrofit.create(IArticleService.class);
    }

    @Provides
    @Singleton
    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.REMOTE_URL)
                .build();
    }
}
