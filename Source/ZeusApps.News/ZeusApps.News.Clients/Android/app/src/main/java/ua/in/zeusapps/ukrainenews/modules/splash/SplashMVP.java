package ua.in.zeusapps.ukrainenews.modules.splash;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface SplashMVP {
    interface IView extends BaseMVP.IView{
        void forceClose();
    }

    interface IPresenter extends BaseMVP.IPresenter<IView>{
        Observable<Boolean> prepare();
    }

    interface IModel extends  BaseMVP.IModel{
        Observable<List<Source>> ensureSources();
    }
}
