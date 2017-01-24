package ua.in.zeusapps.ukrainenews.modules.source;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface SourceMVP {
    interface IView extends BaseMVP.IView{
        void updateSources(List<Source> sources);
    }

    interface IPresenter extends BaseMVP.IPresenter<IView>{

    }

    interface IModel extends BaseMVP.IModel {
        Observable<List<Source>> getSources();
    }
}