package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class ArticlePresenter implements ArticleMVP.IPresenter {

    private final ArticleMVP.IModel _model;
    private ArticleMVP.IView _view;
    private List<Article> _articles;
    private List<Source> _sources;
    private Source _selectedSource;

    ArticlePresenter(ArticleMVP.IModel model) {
        _model = model;
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

        _selectedSource = source;
        loadArticles();
        _view.setChecked(source.getId());
    }

    @Override
    public Source getSelectedSource() {
        return _selectedSource;
    }

    @Override
    public void refresh() {
        if (_sources != null){
            _view.updateSources(_sources);
        } else {
            loadSources();
        }

        if (_articles != null){
            _view.updateArticles(_articles);
            loadNewArticles();
        } else {
            loadArticles();
        }
    }

    private void loadSources(){
        _view.loadStarted();
        _model.getSources().subscribe(new CustomSubscriber<List<Source>>() {
            @Override
            public void onNext(List<Source> sources) {
                _sources = sources;
                _view.updateSources(sources);
                if (_selectedSource == null && _sources.size() > 0){
                    _selectedSource = sources.get(0);
                    loadArticles();
                }
            }
        });
    }

    private void loadArticles(){
        if (_selectedSource == null){
            return;
        }

        _model
                .getArticles(_selectedSource.getKey())
                .subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {
                        if (articles.size() == 0 ||
                                _selectedSource == null ||
                                !articles.get(0).getSourceId().equals(_selectedSource.getKey())){
                            return;
                        }

                        _articles = articles;
                        _view.setChecked(_selectedSource.getId());
                        _view.updateArticles(articles);
                    }
                });
    }

    private void loadNewArticles(){
        if (_selectedSource == null || _articles == null || _articles.size() == 0){
            return;
        }

        Article firstArticle = _articles.get(0);
        _model
                .getNewerArticles(_selectedSource.getKey(), firstArticle)
                .subscribe(new CustomSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {
                        if (articles.size() == 0 ||
                                _selectedSource == null ||
                                !articles.get(0).getSourceId().equals(_selectedSource.getKey())){
                            return;
                        }

                        _articles.addAll(0, articles);
                        _view.addNewerArticles(articles);
                    }
                });
    }

    private void showNetworkError(){
        _view.showError("Network error");
    }

    private abstract class CustomSubscriber<T> extends Subscriber<T>{
        @Override
        public void onCompleted() {
            _view.loadComplete();
        }

        @Override
        public void onError(Throwable e) {
            showNetworkError();
        }
    }
}
