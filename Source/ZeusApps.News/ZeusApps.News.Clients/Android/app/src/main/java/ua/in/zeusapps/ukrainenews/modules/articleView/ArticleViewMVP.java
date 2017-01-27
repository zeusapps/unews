package ua.in.zeusapps.ukrainenews.modules.articleView;

import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ArticleViewMVP {

    interface IView extends BaseMVP.IView {
        void showArticle(Article article, Source source);
    }

    interface IPresenter extends BaseMVP.IPresenter<IView>{
        void showArticle(Article article, Source source);
    }
}
