package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.common.BaseMVP;

public interface MainActivityMVP {
    interface View extends BaseMVP.IView {
    }

    interface Presenter extends BaseMVP.IPresenter<View> {
    }

    interface Model extends BaseMVP.IModel {
    }
}
