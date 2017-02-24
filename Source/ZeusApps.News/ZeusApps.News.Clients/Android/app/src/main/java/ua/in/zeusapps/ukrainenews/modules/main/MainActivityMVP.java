package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface MainActivityMVP {
    interface View extends BaseMVP.IView {
        void switchToArticleView(Article article);
        void switchToSettingsView();
    }

    interface Presenter extends BaseMVP.IPresenter<View> {
        void showArticle(Article article);
    }

    interface Model extends BaseMVP.IModel {
    }
}
