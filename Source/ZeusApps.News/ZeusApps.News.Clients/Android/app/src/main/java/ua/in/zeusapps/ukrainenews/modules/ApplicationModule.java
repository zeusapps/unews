package ua.in.zeusapps.ukrainenews.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.services.IRepository;
import ua.in.zeusapps.ukrainenews.services.Repository;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.IDataService;

@Module
public class ApplicationModule {

    private App application;

    public ApplicationModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return application;
    }

    @Provides
    @Singleton
    public Formatter provideFormatter(Context context){
        return new Formatter(context);
    }

    @Provides
    @Singleton
    public IDataService providesArticleService(Retrofit retrofit){
        return retrofit.create(IDataService.class);
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

    @Provides
    @Singleton
    public IRepository provideRepository(Context context, Formatter formatter){
        //return new Repository(context, formatter);
        return new Repository(context, formatter);
    }
}
