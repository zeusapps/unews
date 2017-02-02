package ua.in.zeusapps.ukrainenews.modules.articles;

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


    private final IArticleService _articleService;
    private final ISourceService _sourcesService;
    private final IRepository _repository;
    private final Subscriber<List<Article>> _cacheSubscriber;
    private List<Source> _cachedSources;

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
    public List<Article> getLocalArticles(String sourceId) {
        return _repository.getAllArticles(sourceId);
    }

    @Override
    public Observable<List<Article>> getArticles(String sourceId) {
        Observable<List<Article>> observable = _articleService
                .getArticles(sourceId, PAGE_SIZE)
                .doOnEach(_cacheSubscriber);

        return wrapObservable(observable);
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
        if (_cachedSources != null){
            return Observable.just(_cachedSources);
        }



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

        observable = observable.map(new Func1<List<Source>, List<Source>>() {
            @Override
            public List<Source> call(List<Source> sources) {
                _cachedSources = sources;
                return sources;
            }
        });

        return wrapObservable(observable);
    }

    private Observable<List<Article>> getArticlePage(
            String sourceId, Article article, boolean isAfter){
        Observable<List<Article>> observable = _articleService
                .getNewerArticles(sourceId, PAGE_SIZE, article.getPublished(), isAfter)
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
