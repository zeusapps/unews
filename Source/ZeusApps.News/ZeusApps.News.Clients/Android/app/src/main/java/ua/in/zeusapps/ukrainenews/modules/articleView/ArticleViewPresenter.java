package ua.in.zeusapps.ukrainenews.modules.articleView;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class ArticleViewPresenter implements ArticleViewMVP.IPresenter {

    private ArticleViewMVP.IView _view;
    private Article _article;
    private Source _source;

    @Override
    public void showArticle(Article article, Source source) {
        _article = article;
        _source = source;
        show();
    }

    @Override
    public void setView(ArticleViewMVP.IView view) {
        _view = view;
        show();
    }

    private void show(){
        if (_view != null && _article != null){
            _view.showArticle(_article, _source);
        }
    }
}
