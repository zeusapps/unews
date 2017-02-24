package ua.in.zeusapps.ukrainenews.modules.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    public MainActivityMVP.Presenter provideMainActivityPresenter(){
        return new MainActivityPresenter();
    }
}
