package ua.in.zeusapps.ukrainenews.modules.articleView;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IRepository;

public class ArticleViewPresenter implements ArticleViewMVP.IPresenter {

    private IRepository _repository;
    private ArticleViewMVP.IView _view;

    public ArticleViewPresenter(IRepository repository){
        _repository = repository;
    }

    @Override
    public void showArticle(String articleId) {
        Article article = _repository.getArticle(articleId);
        show(article);
    }

    @Override
    public void setView(ArticleViewMVP.IView view) {
        _view = view;
    }

    private void show(Article article){
        if (_view == null){
            return;
        }

        Source source = _repository.getSourceByKey(article.getSourceId());
        if (source != null){
            _view.showArticle(article, source);
        }
    }
}