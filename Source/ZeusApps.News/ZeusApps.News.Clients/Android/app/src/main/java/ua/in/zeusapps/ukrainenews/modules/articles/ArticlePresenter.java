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
        updateArticles();
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
            _model.getSources().subscribe(new Subscriber<List<Source>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Source> sources) {
                    _sources = sources;
                    _view.updateSources(sources);
                    if (_selectedSource != null || _sources.size() == 0){
                        return;
                    }

                    _selectedSource = sources.get(0);
                    updateArticles();

                }
            });
        }

        if (_articles != null){
            _view.updateArticles(_articles);
        } else {
            updateArticles();
        }
    }

    private void updateArticles(){
        if (_selectedSource == null){
            return;
        }

        _model
                .getArticles(_selectedSource.getKey())
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        if (articles.size() == 0 ||
                                _selectedSource == null ||
                                !articles.get(0).getSourceId().equals(_selectedSource.getKey())){
                            return;
                        }

                        _view.setChecked(_selectedSource.getId());
                        _articles = articles;
                        _view.updateArticles(articles);

                    }
                });
    }
}
