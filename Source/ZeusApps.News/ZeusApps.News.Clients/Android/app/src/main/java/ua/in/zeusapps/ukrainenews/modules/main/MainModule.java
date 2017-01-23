package ua.in.zeusapps.ukrainenews.modules.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    public MainActivityMVP.Presenter provideMainActivityPresenter(
            MainActivityMVP.Model model){
        return new MainActivityPresenter(model);
    }

    @Provides
    public MainActivityMVP.Model provideMainActivityModel(){
        return new MainActivityModel();
    }

}
