package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.services.IArticleService;

public class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private final IArticleService _articleService;
    private final ArrayList<Article> _tempArticles;
    private String _requestedSourceId;

    public ArticleModel() {
        _articleService = getRetrofit().create(IArticleService.class);
        _tempArticles = new ArrayList<>();
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {

        if (!sourceId.equals(_requestedSourceId)){
            _requestedSourceId = sourceId;
            _tempArticles.clear();
        }

        Observable<List<Article>> observable = _articleService
                .getArticles(sourceId)
                .map(new Func1<List<Article>, List<Article>>() {
                    @Override
                    public List<Article> call(List<Article> articles) {

                        for(Article article: articles){
                            if (!article.getSourceId().equals(_requestedSourceId)){
                                return new ArrayList<>();
                            }

                            if (!_tempArticles.contains(article)){
                                _tempArticles.add(article);
                            }
                        }

                        return articles;
                    }
                });

        return wrapObservable(observable);
    }

    @Override
    public Article getArticle(String id) {

        for (Article article: _tempArticles){
            if (article.getId().equals(id)){
                return article;
            }
        }

        return null;
    }
}
