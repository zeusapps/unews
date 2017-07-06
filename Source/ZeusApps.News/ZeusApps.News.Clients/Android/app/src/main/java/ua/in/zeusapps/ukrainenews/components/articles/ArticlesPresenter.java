package ua.in.zeusapps.ukrainenews.components.articles;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetArticlesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetInitialArticlesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetSourceInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class ArticlesPresenter extends MvpPresenter<ArticlesView, ArticlesRouter> {

    @Inject
    ArticlesRouter _router;
    @Inject
    GetInitialArticlesInteractor _initialArticlesInteractor;
    @Inject
    GetArticlesInteractor _articlesInteractor;
    @Inject
    GetSourceInteractor _sourceInteractor;

    public ArticlesPresenter() {
        getComponent().inject(this);
    }

    @Override
    public ArticlesRouter getRouter() {
        return _router;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        _initialArticlesInteractor.unsubscribe();
        _articlesInteractor.unsubscribe();
    }

    void init(String sourceId) {
        getViewState().showLoading(true);
        _sourceInteractor.execute(source -> {
            getViewState().setSource(source);
            _initialArticlesInteractor.execute(
                    articles -> {
                        getViewState().init(articles);
                        getViewState().showLoading(false);
                    },
                    source);
        }, sourceId);
    }

    void loadNewer(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, false);

        _articlesInteractor.execute(
                response -> {
                    getViewState().addNewer(response.getArticles(), response.getIsRefresh());
                    getViewState().showLoading(false);

                    if (!bundle.getIsAfter() && response.getArticles().size() == 0) {
                        getViewState().showEmptyUpdate();
                    }
                },
                throwable -> {
                    getViewState().showLoading(false);
                    getViewState().showLoadingError();
                },
                bundle);
    }

    void loadOlder(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, true);

        getViewState().showLoading(true);
        _articlesInteractor.execute(
                response -> {
                    getViewState().addOlder(response.getArticles());
                    getViewState().showLoading(false);
                },
                throwable -> {
                    getViewState().showLoading(false);
                    getViewState().showLoadingError();
                },
                bundle);
    }

    void showArticle(Article article, Source source) {
        getRouter().showArticleDetails(article, source);
    }
}
