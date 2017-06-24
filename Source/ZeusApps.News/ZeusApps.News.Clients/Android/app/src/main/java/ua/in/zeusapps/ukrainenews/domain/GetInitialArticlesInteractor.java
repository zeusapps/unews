package ua.in.zeusapps.ukrainenews.domain;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.IDataService;

public class GetInitialArticlesInteractor extends Interactor<List<Article>, Source> {

    private final IDataService _dataService;
    private final IArticleRepository _articleRepository;
    private final ISourceRepository _sourceRepository;
    private final Formatter _formatter;


    private static final int PAGE_SIZE = 20;

    @Inject
    GetInitialArticlesInteractor(
            IDataService dataService,
            IArticleRepository articleRepository,
            ISourceRepository sourceRepository,
            Formatter formatter) {
        _dataService = dataService;
        _articleRepository = articleRepository;
        _sourceRepository = sourceRepository;
        _formatter = formatter;
    }

    @Override
    protected Observable<List<Article>> buildObservable(final Source source) {
        return _articleRepository.getBySource(source)
                .flatMap(articles -> {
                    if (articles.size() == 0){
                        return _dataService.getArticles(source.getKey(), PAGE_SIZE)
                                .map(newArticles -> {
                                    if (newArticles.size() == PAGE_SIZE){
                                        _articleRepository.removeBySource(source);
                                    }
                                    save(articles, source);
                                    return newArticles;
                                });
                    }

                    if (shouldNotUpdate(source)){
                        return Observable.just(articles);
                    }
                    String published = _formatter.formatDate(articles.get(0).getPublished());
                    return _dataService
                            .getNewerArticles(source.getKey(), PAGE_SIZE, published, false)
                            .flatMap(newArticles -> {
                                if (newArticles.size() == PAGE_SIZE){
                                    _articleRepository.removeBySource(source);
                                    save(newArticles, source);
                                    return Observable.just(newArticles);
                                }

                                save(newArticles, source);
                                newArticles.addAll(articles);
                                return Observable.just(newArticles);
                            });
                });
    }

    private void save(List<Article> articles, Source source){
        source.setTimestamp(new Date());
        _sourceRepository.update(source);

        for (Article article: articles) {
            _articleRepository.create(article);
        }
    }

    private boolean shouldNotUpdate(Source source){
        Date nowTimestamp = new Date();
        Date timestamp = source.getTimestamp();
        int timeSkew = 1000 * 60 * 15;

        return timestamp != null  &&
                timestamp.getTime() + timeSkew > nowTimestamp.getTime();
    }
}