package ua.in.zeusapps.ukrainenews.common;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.components.DaggerApplicationComponent;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;

public class App extends Application {

    private static App _instance;

    private ApplicationComponent component;

    public ApplicationComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        ButterKnife.setDebug(true);


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5823688689397702~2489213275");

        _instance = this;
    }

    public static App getInstance(){
        return _instance;
    }
}
