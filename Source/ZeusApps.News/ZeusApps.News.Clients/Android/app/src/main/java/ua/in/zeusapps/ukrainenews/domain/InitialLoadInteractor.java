package ua.in.zeusapps.ukrainenews.domain;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.ObservableInteractor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.models.SourceBundle;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class InitialLoadInteractor
        extends ObservableInteractor<SourceBundle, Void> {

    private final ISourceRepository _sourceRepository;
    private final IArticleRepository _articleRepository;
    private final IDataService _dataService;
    private final Formatter _formatter;
    private final static int ITEMS_COUNT = 20;
    private final Article NullArticle = new Article();

    @Inject
    InitialLoadInteractor(
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
    protected Observable<SourceBundle> build(Void parameter) {
        return _dataService
                .getSources()
                .toObservable()
                .flatMap(sources ->
                        Observable
                                .fromIterable(sources)
                                .flatMap(source -> getBundle(source)
                                        .toObservable()));
    }

    private Single<SourceBundle> getBundle(Source source) {
        return _articleRepository.getNewest(source)
                .onErrorReturn(t -> NullArticle)
                .flatMap(article -> {
                    if (article.equals(NullArticle)) {
                        return _dataService.getArticles(source.getKey(), ITEMS_COUNT);
                    }

                    String published = _formatter.formatDate(article.getPublished());

                    return _dataService.getArticles(source.getKey(), ITEMS_COUNT, published, false);
                })
                .map(articles -> {
                    _sourceRepository.updateTimestamp(source);
                    if (articles.size() == ITEMS_COUNT) {
                        _articleRepository.removeBySource(source);
                    }
                    _articleRepository.create(articles);
                    return new SourceBundle(source, articles);
                });
    }
}