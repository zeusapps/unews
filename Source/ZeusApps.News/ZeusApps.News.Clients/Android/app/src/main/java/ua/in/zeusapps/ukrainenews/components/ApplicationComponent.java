package ua.in.zeusapps.ukrainenews.components;

import javax.inject.Singleton;

import dagger.Component;
import ua.in.zeusapps.ukrainenews.modules.ApplicationModule;
import ua.in.zeusapps.ukrainenews.modules.main.MainActivity;
import ua.in.zeusapps.ukrainenews.modules.main.MainModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        MainModule.class
})
public interface ApplicationComponent {
    void inject(MainActivity target);
}
