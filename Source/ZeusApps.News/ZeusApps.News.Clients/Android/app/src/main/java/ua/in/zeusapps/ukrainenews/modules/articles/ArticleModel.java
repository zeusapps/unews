package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IArticleService;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private static final int PAGE_SIZE = 20;


    private final IArticleService _articleService;
    private final IRepository _repository;
    private final Subscriber<List<Article>> _cacheSubscriber;

    ArticleModel(
            IRepository repository,
            IArticleService articleService) {
        _articleService = articleService;
        _repository = repository;

        _cacheSubscriber = new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Article> articles) {
                _repository.addAllArticles(articles);
            }
        };
    }

    @Override
    public List<Article> getLocalArticles(String sourceId) {
        return _repository
                .getArticlesPage(sourceId, null, PAGE_SIZE);
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {
        Observable<List<Article>> observable = _articleService
                .getArticles(sourceId, PAGE_SIZE)
                .doOnEach(_cacheSubscriber);

        return wrapObservable(observable);
    }

    @Override
    public Observable<List<Article>> getNewerArticles(String sourceId, Article firstArticle) {
        return getArticlePage(sourceId, firstArticle, false);
    }

    @Override
    public Observable<List<Article>> getOlderArticles(String sourceId, Article lastArticle) {
        List<Article> articles = _repository.getArticlesPage(sourceId, lastArticle, PAGE_SIZE);
        if (articles.size() > 0){
            return Observable.just(articles);
        }

        return getArticlePage(sourceId, lastArticle, true);
    }

    @Override
    public List<Source> getSources() {
        return _repository.getAllSources();
    }

    private Observable<List<Article>> getArticlePage(
            String sourceId, Article article, boolean isAfter){
        Observable<List<Article>> observable = _articleService
                .getNewerArticles(sourceId, PAGE_SIZE, article.getPublished(), isAfter)
                .doOnEach(_cacheSubscriber);

        return wrapObservable(observable);
    }


}
