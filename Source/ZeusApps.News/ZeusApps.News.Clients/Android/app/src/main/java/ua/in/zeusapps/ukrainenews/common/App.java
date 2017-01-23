package ua.in.zeusapps.ukrainenews.common;

import android.app.Application;

import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.components.DaggerApplicationComponent;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;

public class App extends Application {
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
    }
}
