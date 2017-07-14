package ua.in.zeusapps.ukrainenews.domain;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.ArticleResponse;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetArticlesInteractor extends SingleInteractor<ArticleResponse, ArticleRequestBundle> {

    private final static int PAGE_COUNT = 20;
    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;
    private final IDataService _dataService;

    @Inject
    GetArticlesInteractor(
            ISourceRepository sourceRepository,
            IArticleRepository articleRepository,
            IDataService dataService) {
        _sourceRepository = sourceRepository;
        _articleRepository = articleRepository;
        _dataService = dataService;
    }

    @Override
    protected Single<ArticleResponse> build(final ArticleRequestBundle bundle) {

        Source source = bundle.getSource();
        long published = bundle.getArticle().getPublished();
        String key = source.getKey();
        boolean isAfter = bundle.getIsAfter();

        return isAfter
                ? getOlderArticles(key, published)
                : getNewerArticles(source, key, published);
    }

    private Single<ArticleResponse> getNewerArticles(
            Source source, String key, long published) {
        return _dataService
                .getArticles(key, PAGE_COUNT, published, false)
                .map(articles -> {
                    _sourceRepository.updateTimestamp(source);
                    boolean refresh = false;
                    if (articles.size() == PAGE_COUNT) {
                        _articleRepository.removeBySource(source);
                        refresh = true;
                    }
                    _articleRepository.create(articles);
                    return new ArticleResponse(articles, refresh);
                });
    }

    private Single<ArticleResponse> getOlderArticles(String key, long published) {
        return _dataService
                .getArticles(key, PAGE_COUNT, published, true)
                .map(articles -> {
                    _articleRepository.create(articles);
                    return new ArticleResponse(articles, false);
                });
    }
}
