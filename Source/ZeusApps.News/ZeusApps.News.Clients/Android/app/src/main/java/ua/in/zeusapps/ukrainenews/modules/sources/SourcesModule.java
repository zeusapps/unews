package ua.in.zeusapps.ukrainenews.modules.sources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ua.in.zeusapps.ukrainenews.services.IRepository;

@Module
public class SourcesModule {

    @Provides
    @Singleton
    public SourcesModel providesSourcesModel(IRepository repository){
        return new SourcesModel(repository);
    }

}
