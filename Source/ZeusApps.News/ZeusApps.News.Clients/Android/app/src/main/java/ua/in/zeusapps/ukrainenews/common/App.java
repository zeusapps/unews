package ua.in.zeusapps.ukrainenews.common;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.twitter.sdk.android.core.Twitter;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import ua.in.zeusapps.ukrainenews.R;
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


        MobileAds.initialize(getApplicationContext(), getString(R.string.AD_APP_ID));
        Twitter.initialize(this);
        Fabric.with(this, new Crashlytics());

        _instance = this;
    }

    public static App getInstance(){
        return _instance;
    }
}
