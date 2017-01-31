package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IArticleService;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

public class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private static final int PAGE_SIZE = 20;

    private final IArticleService _articleService;
    private final ISourceService _sourcesService;


    public ArticleModel() {
        _articleService = getRetrofit().create(IArticleService.class);
        _sourcesService = getRetrofit().create(ISourceService.class);
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {
        Observable<List<Article>> observable = _articleService.getArticles(sourceId, PAGE_SIZE);

        return wrapObservable(observable);
    }

    @Override
    public Observable<List<Article>> getNewerArticles(String sourceId, Article firstArticle) {
        return getArticlePage(sourceId, firstArticle, false);
    }

    @Override
    public Observable<List<Article>> getOlderArticles(String sourceId, Article lastArticle) {
        return getArticlePage(sourceId, lastArticle, true);
    }


    @Override
    public Observable<List<Source>> getSources() {
        Observable<List<Source>> observable = _sourcesService.getSources();
        return wrapObservable(observable);
    }

    private Observable<List<Article>> getArticlePage(String sourceId, Article article, boolean isAfter){
        Observable<List<Article>> observable =
                _articleService.getNewerArticles(sourceId, PAGE_SIZE, article.getPublished(), isAfter);

        return wrapObservable(observable);
    }
}
