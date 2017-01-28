package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ArticleMVP {
    interface IView extends BaseMVP.IView{
        void updateArticles(List<Article> articles);
        void updateSources(List<Source> sources);
    }

    interface IPresenter extends BaseMVP.IPresenter<ArticleMVP.IView>{
        void setSelectedSource(Source source);
        Source getSelectedSource();
        void refresh();
    }

    interface IModel extends BaseMVP.IModel {
        Observable<List<Article>> getArticles(String sourceId);
        Observable<List<Source>> getSources();
    }
}
