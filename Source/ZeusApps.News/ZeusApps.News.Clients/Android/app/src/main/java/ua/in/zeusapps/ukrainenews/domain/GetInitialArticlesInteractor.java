package ua.in.zeusapps.ukrainenews.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetInitialArticlesInteractor extends SingleInteractor<List<Article>, Source> {

    private final IDataService _dataService;
    private final IArticleRepository _articleRepository;
    private final ISourceRepository _sourceRepository;

    private static final int PAGE_SIZE = 20;

    @Inject
    GetInitialArticlesInteractor(
            IDataService dataService,
            IArticleRepository articleRepository,
            ISourceRepository sourceRepository) {
        _dataService = dataService;
        _articleRepository = articleRepository;
        _sourceRepository = sourceRepository;
    }

    @Override
    protected Single<List<Article>> build(final Source source) {
        return _articleRepository.getBySource(source)
            .flatMap(articles -> {
                if (articles.size() == 0){
                    return getInitialRemoteArticles(source);
                }

                return _sourceRepository.shouldUpdate(source)
                    ? getNewerArticles(source, articles)
                    : Single.just(articles);
            });
    }

    private Single<List<Article>> getInitialRemoteArticles(Source source){
        return _dataService.getArticles(source.getKey(), PAGE_SIZE)
                .onErrorReturn(throwable -> new ArrayList<>())
                .map(articles -> {
                    save(articles, source);
                    return articles;
                });
    }

    private Single<List<Article>> getNewerArticles(Source source, List<Article> olderArticles){
        Article article = olderArticles.get(0);

        return _dataService
                .getArticles(source.getKey(), PAGE_SIZE, article.getPublished(), false)
                .onErrorReturn(throwable -> new ArrayList<>())
                .map(articles -> {
                    if (articles.size() == PAGE_SIZE){
                        _articleRepository.removeBySource(source);
                        save(articles, source);
                        return articles;
                    }

                    save(articles, source);
                    articles.addAll(olderArticles);
                    return articles;
                });
    }

    private void save(List<Article> articles, Source source){
        _sourceRepository.updateTimestamp(source);
        _articleRepository.create(articles);
    }
}