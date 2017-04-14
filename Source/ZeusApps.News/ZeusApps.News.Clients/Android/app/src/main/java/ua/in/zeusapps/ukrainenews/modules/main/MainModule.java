package ua.in.zeusapps.ukrainenews.modules.main;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.ISourceRepository;
import ua.in.zeusapps.ukrainenews.services.SourceRepository;

@Module
public class MainModule {

    @Provides
    public MainActivityMVP.Presenter provideMainActivityPresenter(){
        return new MainActivityPresenter();
    }

    @Provides
    public ISourceRepository providesSourceRepository(Context context){
        return new SourceRepository(context);
    }
}
