package ua.in.zeusapps.ukrainenews.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.Constants;
import ua.in.zeusapps.ukrainenews.common.GsonUTCDateAdapter;
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .baseUrl(Constants.REMOTE_URL)
                .build();
    }
}
