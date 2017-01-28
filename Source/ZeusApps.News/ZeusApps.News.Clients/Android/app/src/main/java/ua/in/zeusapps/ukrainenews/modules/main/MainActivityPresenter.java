package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewMVP;

class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;
    private ArticleViewMVP.IPresenter _articleViewPresenter;

    MainActivityPresenter(ArticleViewMVP.IPresenter articleViewPresenter) {
        _articleViewPresenter = articleViewPresenter;
    }

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;
    }

    @Override
    public void updateArticle(Source source, Article article) {
        if (article == null || source == null){
            return;
        }

        _articleViewPresenter.showArticle(article, source);
        _view.switchToArticleView();
    }
}
