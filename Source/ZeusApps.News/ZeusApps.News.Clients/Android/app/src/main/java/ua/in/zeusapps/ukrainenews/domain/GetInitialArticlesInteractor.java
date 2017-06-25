package ua.in.zeusapps.ukrainenews.domain;

import java.util.ArrayList;
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
                    return getInitialRemoteArticles(source);
                }

                return shouldNotUpdate(source)
                    ? Observable.just(articles)
                    : getNewerArticles(source, articles);
            });
    }

    private Observable<List<Article>> getInitialRemoteArticles(Source source){
        return _dataService.getArticles(source.getKey(), PAGE_SIZE)
            .onErrorReturn(throwable -> new ArrayList<>())
            .map(articles -> {
                save(articles, source);
                return articles;
            });
    }

    private Observable<List<Article>> getNewerArticles(Source source, List<Article> olderArticles){
        String published = _formatter.formatDate(olderArticles.get(0).getPublished());

        return _dataService
                .getArticles(source.getKey(), PAGE_SIZE, published, false)
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