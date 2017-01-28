package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface MainActivityMVP {
    interface View extends BaseMVP.IView {
        void switchToArticleView();
    }

    interface Presenter extends BaseMVP.IPresenter<View> {
        void updateSource(Source source);
        void updateArticle(Article article);
    }

    interface Model extends BaseMVP.IModel {
    }
}
