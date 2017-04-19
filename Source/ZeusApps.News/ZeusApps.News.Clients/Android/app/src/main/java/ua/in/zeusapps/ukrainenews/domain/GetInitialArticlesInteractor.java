package ua.in.zeusapps.ukrainenews.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
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
        return getLocal(source)
                .flatMap(new Func1<List<Article>, Observable<List<Article>>>() {
                    @Override
                    public Observable<List<Article>> call(List<Article> articles) {
                        return getNewerOrAll(source, articles);
                    }
                })
                .flatMap(new Func1<List<Article>, Observable<List<Article>>>() {
                    @Override
                    public Observable<List<Article>> call(List<Article> articles) {
                        saveNewArticles(articles, source);
                        return getLocal(source);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Article>>>() {
                    @Override
                    public Observable<? extends List<Article>> call(Throwable throwable) {
                        return getLocal(source);
                    }
                });
    }

    private Observable<List<Article>> getLocal(Source source){
        return _articleRepository.getBySource(source);
    }

    private Observable<List<Article>> getNewerOrAll(Source source, List<Article> local){
        if (local.size() == 0) {
            return _dataService.getArticles(source.getKey(), PAGE_SIZE);
        }

        if (shouldNotUpdate(source)){
            List<Article> emptyResult = new ArrayList<>();
            return Observable.just(emptyResult);
        }

        return getNewerArticles(source, local.get(0));
    }

    private void saveNewArticles(List<Article> articles, Source source){
        if (articles.size() == PAGE_SIZE){
            _articleRepository.removeBySource(source);
        }

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

    private Observable<List<Article>> getNewerArticles(Source source, Article firstArticle){
        source.setTimestamp(new Date());
        _sourceRepository.update(source);
        String dateString = _formatter.toStringDate(firstArticle.getPublished());
        return _dataService.getNewerArticles(
                source.getKey(), PAGE_SIZE, dateString, false);
    }
}