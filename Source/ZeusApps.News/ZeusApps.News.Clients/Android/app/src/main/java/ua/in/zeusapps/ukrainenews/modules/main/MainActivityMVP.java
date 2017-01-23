package ua.in.zeusapps.ukrainenews.modules.main;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface MainActivityMVP {
    interface View {

        void updateSources(List<Source> sources);

    }

    interface Presenter{

        void setView(MainActivityMVP.View view);

    }

    interface Model{
        Observable<List<Source>> getSources();
    }
}
