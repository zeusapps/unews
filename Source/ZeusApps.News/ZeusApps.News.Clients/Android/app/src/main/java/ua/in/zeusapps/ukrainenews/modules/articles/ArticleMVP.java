package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.source.SourceMVP;

public interface ArticleMVP {
    interface IView extends BaseMVP.IView{
        void updateArticles(List<Article> articles);
    }

    interface IPresenter extends BaseMVP.IPresenter<ArticleMVP.IView>{
        void updateArticles(String sourceId);
        void getArticles();
    }

    interface IModel extends BaseMVP.IModel {
        Observable<List<Article>> getArticles(String sourceId);

        Article getArticle(String id);
    }
}
