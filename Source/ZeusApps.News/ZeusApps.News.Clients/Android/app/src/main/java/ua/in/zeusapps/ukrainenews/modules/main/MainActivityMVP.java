package ua.in.zeusapps.ukrainenews.modules.main;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface MainActivityMVP {
    interface View extends BaseMVP.IView {
        void updateSources(List<Source> sources);
    }

    interface Presenter extends BaseMVP.IPresenter<View> {

    }

    interface Model extends BaseMVP.IModel {
        Observable<List<Source>> getSources();
    }
}
