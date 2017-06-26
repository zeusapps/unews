package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;


import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.domain.GetArticlesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetInitialArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<ArticleView, MainRouter> {

    @Inject
    GetInitialArticlesInteractor initialArticlesInteractor;
    @Inject
    GetArticlesInteractor articlesInteractor;
    @Inject
    ISourceRepository sourceRepository;
    @Inject
    MainRouter router;

    ArticlePresenter() {
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        initialArticlesInteractor.unsubscribe();
        articlesInteractor.unsubscribe();
    }

    void init(String sourceId){
        getViewState().showLoading(true);
        Source source = sourceRepository.getById(sourceId);
        getViewState().setSource(source);
        initialArticlesInteractor.execute(
                articles -> {
                    getViewState().init(articles);
                    getViewState().showLoading(false);
                },
                source);
    }

    void loadNewer(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, false);

        articlesInteractor.execute(
                response -> {
                    getViewState().addNewer(response.getArticles(), response.getIsRefresh());
                    getViewState().showLoading(false);

                    if (!bundle.getIsAfter() && response.getArticles().size() == 0){
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
        articlesInteractor.execute(
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

    void showArticle(Article article, Source source){
        getRouter().showArticleDetails(article, source);
    }
}
