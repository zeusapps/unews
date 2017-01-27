package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.models.Article;

public class ArticlePresenter implements ArticleMVP.IPresenter {

    private final ArticleMVP.IModel _model;
    private ArticleMVP.IView _view;
    private List<Article> _tempArticles;

    public ArticlePresenter(ArticleMVP.IModel model) {
        _model = model;
    }

    @Override
    public void setView(ArticleMVP.IView view) {
        _view = view;
    }

    @Override
    public void updateArticles(String sourceId) {
        _model
                .getArticles(sourceId)
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        _tempArticles = articles;
                        _view.updateArticles(articles);
                    }
                });
    }

    @Override
    public void getArticles() {
        if (_tempArticles != null){
            _view.updateArticles(_tempArticles);
        }
    }
}
