package ua.in.zeusapps.ukrainenews.modules.articles;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IArticleService;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

class ArticleModel extends BaseModel implements ArticleMVP.IModel {

    private static final int PAGE_SIZE = 20;
    private static final int UPDATE_PERIOD = 15 * 60 * 1000;

    private final IArticleService _articleService;
    private final ISourceService _sourcesService;
    private final IRepository _repository;
    private final Subscriber<List<Article>> _cacheSubscriber;
    private long _last_updated_timestamp;

    ArticleModel(
            IRepository repository,
            ISourceService sourceService,
            IArticleService articleService) {
        _articleService = articleService;
        _sourcesService = sourceService;
        _repository = repository;

        _cacheSubscriber = new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Article> articles) {
                _repository.addAllArticles(articles);
            }
        };
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {
        final List<Article> tempArticles = _repository.getAllArticles(sourceId);
        if (tempArticles.size() > 0){

            long timestamp = System.currentTimeMillis();
            if (timestamp < _last_updated_timestamp + UPDATE_PERIOD){
                return Observable.just(tempArticles);
            }

            _last_updated_timestamp = timestamp;
            return getUpdatedArticlesList(sourceId, tempArticles);
        }

        return getFreshArticlesList(sourceId);
    }

    @Override
    public Observable<List<Article>> getNewerArticles(String sourceId, Article firstArticle) {
        return getArticlePage(sourceId, firstArticle, false);
    }

    @Override
    public Observable<List<Article>> getOlderArticles(String sourceId, Article lastArticle) {
        return getArticlePage(sourceId, lastArticle, true);
    }

    @Override
    public Observable<List<Source>> getSources() {
        final List<Source> tempSources = _repository.getAllSources();

        Observable<List<Source>> observable = _sourcesService
                .getSources()
                .map(new Func1<List<Source>, List<Source>>() {
                    @Override
                    public List<Source> call(List<Source> sources) {
                        List<Source> deleteSources = subtract(tempSources, sources);
                        List<Source> addSources = subtract(sources, tempSources);

                        _repository.deleteAllSources(deleteSources);
                        _repository.addAllSources(addSources);

                        return sources;
                    }
                });

        if (tempSources.size() > 0){
            observable = observable.onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Source>>>() {
                @Override
                public Observable<? extends List<Source>> call(Throwable throwable) {
                    return Observable.just(tempSources);
                }
            });

        }

        return wrapObservable(observable);
    }

    private Observable<List<Article>> getArticlePage(
            String sourceId, Article article, boolean isAfter){
        Observable<List<Article>> observable = _articleService
                .getNewerArticles(sourceId, PAGE_SIZE, article.getPublished(), isAfter)
                .doOnEach(_cacheSubscriber);

        return wrapObservable(observable);
    }

    @NonNull
    private Observable<List<Article>> getUpdatedArticlesList(
            String sourceId, final List<Article> tempArticles){
        final Article article = tempArticles.get(0);
        return getNewerArticles(sourceId, article)
                .map(new Func1<List<Article>, List<Article>>() {
                    @Override
                    public List<Article> call(List<Article> articles) {
                        articles.addAll(tempArticles);
                        return articles;
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Article>>>() {
                    @Override
                    public Observable<? extends List<Article>> call(Throwable throwable) {
                        return Observable.just(tempArticles);
                    }
                });
    }

    private Observable<List<Article>> getFreshArticlesList(String sourceId){
        Observable<List<Article>> observable = _articleService
                .getArticles(sourceId, PAGE_SIZE)
                .doOnEach(_cacheSubscriber);

        return wrapObservable(observable);
    }

    private List<Source> subtract(List<Source> first, List<Source> second){
        List<Source> result = new ArrayList<>();

        for (Source firstSource: first) {
            boolean found = false;
            for (Source secondSource: second){
                if (firstSource.equals(secondSource)){
                    found = true;
                    break;
                }
            }
            if (!found){
                result.add(firstSource);
            }
        }

        return result;
    }
}
