package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IArticleService;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

public class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private final IArticleService _articleService;
    private final ISourceService _sourcesService;


    public ArticleModel() {
        _articleService = getRetrofit().create(IArticleService.class);
        _sourcesService = getRetrofit().create(ISourceService.class);
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {

        Observable<List<Article>> observable = _articleService.getArticles(sourceId);

        return wrapObservable(observable);
    }

    @Override
    public Observable<List<Source>> getSources() {
        Observable<List<Source>> observable = _sourcesService.getSources();

        return wrapObservable(observable);
    }
}
