package ua.in.zeusapps.ukrainenews.modules.articleView;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.articles.IRepository;

public class ArticleViewPresenter implements ArticleViewMVP.IPresenter {

    private IRepository _repository;
    private ArticleViewMVP.IView _view;
    private Article _article;

    public ArticleViewPresenter(IRepository repository){
        _repository = repository;
    }

    @Override
    public void showArticle(Article article) {
        _article = article;
        show();
    }

    @Override
    public void setView(ArticleViewMVP.IView view) {
        _view = view;
        show();
    }

    private void show(){
        if (_view == null || _article == null){
            return;
        }

        Source source = _repository.getSourceByKey(_article.getSourceId());
        if (source != null){
            _view.showArticle(_article, source);
        }
    }
}