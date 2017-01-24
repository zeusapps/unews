package ua.in.zeusapps.ukrainenews.modules.source;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SourceModule {

    @Provides
    public SourceMVP.IPresenter providesPresenter(SourceMVP.IModel model){
        return new SourcePresenter(model);
    }

    @Provides
    @Singleton
    public SourceMVP.IModel providesModel(){
        return new SourceModel();
    }
}
