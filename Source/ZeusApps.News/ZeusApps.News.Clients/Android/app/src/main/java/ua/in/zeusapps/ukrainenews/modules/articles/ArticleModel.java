//package ua.in.zeusapps.ukrainenews.modules.articles;
//
//import java.util.List;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.functions.Action1;
//import ua.in.zeusapps.ukrainenews.common.BaseModel;
//import ua.in.zeusapps.ukrainenews.models.Article;
//import ua.in.zeusapps.ukrainenews.models.Source;
//import ua.in.zeusapps.ukrainenews.services.IDataService;
//import ua.in.zeusapps.ukrainenews.services.IRepository;
//
//class ArticleModel extends BaseModel implements ArticleMVP.IModel {
//
//    private static final int PAGE_SIZE = 20;
//
//
//    private final IDataService _articleService;
//    private final IRepository _repository;
//    private final Subscriber<List<Article>> _cacheSubscriber;
//
//    ArticleModel(
//            IRepository repository,
//            IDataService articleService) {
//        _articleService = articleService;
//        _repository = repository;
//
//        _cacheSubscriber = new Subscriber<List<Article>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<Article> articles) {
//                _repository.addAllArticles(articles);
//            }
//        };
//    }
//
//    @Override
//    public List<Article> getLocalArticles(String sourceId) {
//        return _repository
//                .getAllArticles(sourceId);
//    }
//
//    @Override
//    public Observable<List<Article>> getArticles(String sourceId) {
//        Observable<List<Article>> observable = _articleService
//                .getArticles(sourceId, PAGE_SIZE)
//                .doOnEach(_cacheSubscriber);
//
//        return wrapObservable(observable);
//    }
//
//    @Override
//    public Observable<List<Article>> getNewerArticles(Article firstArticle) {
//        return getArticlePage(firstArticle, false);
//    }
//
//    @Override
//    public Observable<List<Article>> getOlderArticles(Article lastArticle) {
//        return getArticlePage(lastArticle, true);
//    }
//
//    @Override
//    public List<Source> getSources() {
//        return _repository.getAllSources();
//    }
//
//    private Observable<List<Article>> getArticlePage(
//            final Article article, final boolean isAfter){
//        Observable<List<Article>> observable = _articleService
//                .getNewerArticles(article.getSourceId(), PAGE_SIZE, article.getPublished(), isAfter)
//                .doOnEach(_cacheSubscriber);
//
//        return wrapObservable(observable);
//    }
//
//
//}
