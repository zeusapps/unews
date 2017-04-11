package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ArticleMVP {
    interface IView extends BaseMVP.IView{
        void updateArticles(List<Article> articles);
        void addNewerArticles(List<Article> articles);
        void addOlderArticles(List<Article> articles);
        void updateSources(List<Source> sources);
        void showError();
        void loadStarted();
        void loadComplete();
    }

    interface IPresenter extends BaseMVP.IPresenter<ArticleMVP.IView>{
        void setSelectedSource(Source source);
        Source getSelectedSource();
        void loadOlder(Article lastArticle);
        void loadNewer(Article firstArticle);

        void initLoad();
        void showArticle();
    }

    interface IModel extends BaseMVP.IModel {
        List<Article> getLocalArticles(String sourceId);

        Observable<List<Article>> getArticles(String sourceId);
        Observable<List<Article>> getNewerArticles(Article firstArticle);
        Observable<List<Article>> getOlderArticles(Article lastArticle);
        List<Source> getSources();
    }
}
