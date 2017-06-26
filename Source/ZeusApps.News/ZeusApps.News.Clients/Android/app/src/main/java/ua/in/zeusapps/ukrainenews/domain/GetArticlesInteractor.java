package ua.in.zeusapps.ukrainenews.domain;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.ArticleResponse;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class GetArticlesInteractor extends SingleInteractor<ArticleResponse, ArticleRequestBundle> {

    private final static int PAGE_COUNT = 20;
    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;
    private final IDataService _dataService;
    private final Formatter _formatter;

    @Inject
    GetArticlesInteractor(
            ISourceRepository sourceRepository,
            IArticleRepository articleRepository,
            IDataService dataService,
            Formatter formatter) {
        _sourceRepository = sourceRepository;
        _articleRepository = articleRepository;
        _dataService = dataService;
        _formatter = formatter;
    }

    @Override
    protected Single<ArticleResponse> build(final ArticleRequestBundle bundle) {

        Source source = bundle.getSource();
        String published = _formatter.toStringDate(bundle.getArticle().getPublished());
        String key = source.getKey();
        boolean isAfter = bundle.getIsAfter();

        return isAfter
                ? getOlderArticles(key, published)
                : getNewerArticles(source, key, published);
    }

    private Single<ArticleResponse> getNewerArticles(
            Source source, String key, String published){
        return _dataService
            .getArticles(key, PAGE_COUNT, published, false)
            .map(articles -> {
                _sourceRepository.updateTimestamp(source);
                boolean refresh = false;

                if (articles.size() == PAGE_COUNT){
                    _articleRepository.removeBySource(source);
                    refresh = true;
                }

                save(articles);
                return new ArticleResponse(articles, refresh);
            });
    }

    private Single<ArticleResponse> getOlderArticles(String key, String published){
        return _dataService.getArticles(key, PAGE_COUNT, published, true)
                .map(articles -> {
                    save(articles);
                    return new ArticleResponse(articles, false);
                });
    }

    private void save(List<Article> articles) {
        for (Article article : articles) {
            _articleRepository.create(article);
        }
    }
}
