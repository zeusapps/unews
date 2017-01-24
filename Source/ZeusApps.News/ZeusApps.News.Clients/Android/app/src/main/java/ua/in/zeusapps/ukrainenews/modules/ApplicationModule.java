package ua.in.zeusapps.ukrainenews.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.common.App;

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
}
