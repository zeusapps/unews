package ua.in.zeusapps.ukrainenews.modules.articles;


import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.domain.GetInitialArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class ArticlePresenter extends MvpPresenterBase<ArticleView> {

    @Inject
    GetInitialArticlesInteractor initialArticlesInteractor;

    public ArticlePresenter() {
        getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        initialArticlesInteractor.execute(new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Article> articles) {
                getViewState().init(articles);
            }
        });
    }

    public void loadNewer(Source source, Article article) {
        getViewState().showLoading(true);
        getViewState().addNewer(null);
    }

    public void loadOlder(Source source, Article lastArticle) {
        getViewState().showLoading(true);
        getViewState().addOlder(null);
    }

//    private static final int UPDATE_PERIOD = 15 * 60 * 1000;
//
//    private final ArticleMVP.IModel _model;
//    private final CompositeSubscription _compositeSubscription;
//    private final HashMap<String, Long> _lastUpdatedTimestamps;
//
//    private ArticleMVP.IView _view;
//    private Source _selectedSource;
//
//
//
//    ArticlePresenter(ArticleMVP.IModel model) {
//        _model = model;
//        _compositeSubscription = new CompositeSubscription();
//
//        _lastUpdatedTimestamps = new HashMap<>();
//    }
//
//    @Override
//    public void setView(ArticleMVP.IView view) {
//        _view = view;
//    }
//
//    @Override
//    public void setSelectedSource(Source source) {
//        if (source == null || source.equals(_selectedSource)){
//            return;
//        }
//
//        _compositeSubscription.clear();
//        _selectedSource = source;
//        _view.updateArticles(new ArrayList<Article>());
//        _view.loadStarted();
//        updateArticles(source.getKey());
//    }
//
//    @Override
//    public Source getSelectedSource() {
//        return _selectedSource;
//    }
//
//    @Override
//    public void loadOlder(Article lastArticle) {
//        _view.loadStarted();
//        Subscription subscription = _model
//                .getOlderArticles(lastArticle)
//                .subscribe(new CustomSubscriber<List<Article>>() {
//                    @Override
//                    public void onNext(List<Article> articles) {
//                        _view.addOlderArticles(articles);
//                    }
//                });
//        _compositeSubscription.add(subscription);
//    }
//
//    @Override
//    public void loadNewer(Article firstArticle) {
//        if (firstArticle == null){
//            initLoad();
//            return;
//        }
//
//        addLastUpdate(firstArticle.getSourceId());
//        Subscription subscription = _model
//                .getNewerArticles(firstArticle)
//                .subscribe(new CustomSubscriber<List<Article>>() {
//                    @Override
//                    public void onNext(List<Article> articles) {
//                        _view.addNewerArticles(articles);
//                    }
//                });
//        _compositeSubscription.add(subscription);
//    }
//
//    @Override
//    public void initLoad() {
//        _view.loadStarted();
//
//        List<Source> sources = _model.getSources();
//        if (sources.size() == 0){
//            _view.loadComplete();
//            return;
//        }
//
//
//        if (_selectedSource == null || !sources.contains(_selectedSource)){
//            _selectedSource = sources.get(0);
//        }
//
//        _view.updateSources(sources);
//        updateArticles(_selectedSource.getKey());
//    }
//
//    @Override
//    public void showArticle() {
//        _compositeSubscription.clear();
//    }
//
//    private void updateArticles(String sourceId){
//        List<Article> articles = _model.getLocalArticles(sourceId);
//        if (articles.size() > 0){
//            _view.updateArticles(articles);
//            if (articleUpdateNeeded(sourceId)){
//                loadNewer(articles.get(0));
//            } else {
//                _view.loadComplete();
//            }
//        } else {
//            Subscription subscription = _model.getArticles(sourceId)
//                    .subscribe(new CustomSubscriber<List<Article>>() {
//                        @Override
//                        public void onNext(List<Article> articles) {
//                            _view.updateArticles(articles);
//                        }
//                    });
//            _compositeSubscription.add(subscription);
//        }
//    }
//
//    private boolean articleUpdateNeeded(String sourceId){
//        if (!_lastUpdatedTimestamps.containsKey(sourceId)){
//            _lastUpdatedTimestamps.put(sourceId, 0L);
//            return true;
//        }
//
//        long timestamp = System.currentTimeMillis();
//        long updateTimestamp = _lastUpdatedTimestamps.get(sourceId);
//        return timestamp >= updateTimestamp + UPDATE_PERIOD;
//    }
//
//    private void showNetworkError(){
//        _view.showError();
//    }
//
//    private void addLastUpdate(String sourceId){
//        _lastUpdatedTimestamps.put(sourceId, System.currentTimeMillis());
//    }
//
//    private abstract class CustomSubscriber<T> extends Subscriber<T>{
//        @Override
//        public void onCompleted() {
//            _view.loadComplete();
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//
//            showNetworkError();
//            _view.loadComplete();
//        }
//    }
}
