package ua.in.zeusapps.ukrainenews.common;

public interface BaseMVP {
    interface IView {
    }

    interface IPresenter<T extends IView> {
        void setView(T view);
    }

    interface IModel {
    }
}
