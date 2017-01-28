package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewMVP;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleMVP;

class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;
    private Source _selectedSource;
    private Article _selectedArticle;

    private ArticleMVP.IPresenter _articlePresenter;
    private ArticleViewMVP.IPresenter _articleViewPresenter;

    MainActivityPresenter(
            ArticleMVP.IPresenter articlePresenter,
            ArticleViewMVP.IPresenter articleViewPresenter) {
        _articlePresenter = articlePresenter;
        _articleViewPresenter = articleViewPresenter;
    }

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;
    }

    @Override
    public void updateSource(Source source) {
        if (source == null){
            return;
        }

        _selectedSource = source;
        _articlePresenter.updateArticles(source.getKey());
    }

    @Override
    public void updateArticle(Article article) {
        if (article == null){
            return;
        }

        _selectedArticle = article;
        if (_selectedSource == null){
            return;
        }

        _articleViewPresenter.showArticle(article, _selectedSource);
        _view.switchToArticleView();
    }
}
