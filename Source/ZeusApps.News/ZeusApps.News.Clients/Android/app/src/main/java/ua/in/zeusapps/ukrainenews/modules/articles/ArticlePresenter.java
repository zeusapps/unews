package ua.in.zeusapps.ukrainenews.modules.articles;

import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class ArticlePresenter implements ArticleMVP.IPresenter {

    private static final int UPDATE_PERIOD = 15 * 60 * 1000;

    private final ArticleMVP.IModel _model;
    private final CompositeSubscription _compositeSubscription;
    private final HashMap<String, Long> _lastUpdatedTimestamps;

    private ArticleMVP.IView _view;
    private Source _selectedSource;



    ArticlePresenter(ArticleMVP.IModel model) {
        _model = model;
        _compositeSubscription = new CompositeSubscription();

        _lastUpdatedTimestamps = new HashMap<>();
    }

    @Override
    public void setView(ArticleMVP.IView view) {
        _view = view;
    }

    @Override
    public void setSelectedSource(Source source) {
        if (source == null || source.equals(_selectedSource)){
            return;
        }

        _compositeSubscription.clear();

        _selectedSource = source;
        _view.updateArticles(new ArrayList<Article>());
        _view.loadStarted();
        Subscription subscription = getLocalAndUpdateArticles(_selectedSource.getKey())
                .subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) { }
                });
        _compositeSubscription.add(subscription);
    }

    @Override
    public Source getSelectedSource() {
        return _selectedSource;
    }

    @Override
    public void loadOlder(Article lastArticle) {
        Subscription subscription = _model
                .getOlderArticles(lastArticle.getSourceId(), lastArticle)
                .subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {
                        _view.addOlderArticles(articles);
                    }
                });
        _compositeSubscription.add(subscription);
    }

    @Override
    public void loadNewer(Article firstArticle) {
        addLastUpdate(firstArticle.getSourceId());
        Subscription subscription = _model
                .getNewerArticles(firstArticle.getSourceId(), firstArticle)
                .subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {
                        _view.addNewerArticles(articles);
                    }
                });
        _compositeSubscription.add(subscription);
    }

    @Override
    public void initLoad() {
        _view.loadStarted();
        Subscription subscription = _model
                .getSources()
                .flatMap(new Func1<List<Source>, Observable<List<Article>>>() {
                    @Override
                    public Observable<List<Article>> call(List<Source> sources) {
                        _view.updateSources(sources);
                        if (sources.size() > 0){
                            if (_selectedSource == null || !sources.contains(_selectedSource)){
                                _selectedSource = sources.get(0);
                            }
                            String sourceId = _selectedSource.getKey();
                            return getLocalAndUpdateArticles(sourceId);
                        }
                        return Observable.just(null);
                    }
                }).subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {  }
                });
        _compositeSubscription.add(subscription);
    }

    @Override
    public void showArticle() {
        _compositeSubscription.clear();
    }

    private Observable<List<Article>> getLocalAndUpdateArticles(String sourceId){
        List<Article> articles = _model.getLocalArticles(sourceId);
        if (articles.size() == 0){
            return _model.getArticles(sourceId);
        } else {
            _view.updateArticles(articles);

            if (!_lastUpdatedTimestamps.containsKey(sourceId)){
                _lastUpdatedTimestamps.put(sourceId, 0L);
            }

            long timestamp = System.currentTimeMillis();
            long updateTimestamp = _lastUpdatedTimestamps.get(sourceId);
            if (timestamp < updateTimestamp + UPDATE_PERIOD){
                return Observable.just(null);
            }

            _lastUpdatedTimestamps.put(sourceId, timestamp);
            return _model
                    .getNewerArticles(_selectedSource.getKey(), articles.get(0))
                    .map(new Func1<List<Article>, List<Article>>() {
                        @Override
                        public List<Article> call(List<Article> articles) {
                            _view.addNewerArticles(articles);
                            return articles;
                        }
                    });
        }
    }

    private void showNetworkError(){
        _view.showError("Network error");
    }

    private void addLastUpdate(String sourceId){
        _lastUpdatedTimestamps.put(sourceId, System.currentTimeMillis());
    }

    private abstract class CustomSubscriber<T> extends Subscriber<T>{
        @Override
        public void onCompleted() {
            _view.loadComplete();
        }

        @Override
        public void onError(Throwable e) {
            showNetworkError();
            _view.loadComplete();
        }
    }
}
