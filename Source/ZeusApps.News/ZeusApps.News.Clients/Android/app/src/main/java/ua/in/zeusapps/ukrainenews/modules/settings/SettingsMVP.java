package ua.in.zeusapps.ukrainenews.modules.settings;

import ua.in.zeusapps.ukrainenews.common.BaseMVP;

public interface SettingsMVP {
    interface View extends BaseMVP.IView {

    }

    interface Presenter extends BaseMVP.IPresenter<View> {
        void clearStorage();
    }
}
