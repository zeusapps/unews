package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.services.IArticleService;

public class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private final IArticleService _articleService;

    public ArticleModel() {
        _articleService = getRetrofit().create(IArticleService.class);
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {
        return wrapObservable(_articleService.getArticles(sourceId));
    }
}
