package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;


import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
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
    MainRouter router;


    ArticlePresenter() {
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }

    void init(Source source){
        getViewState().showLoading(true);
        initialArticlesInteractor.execute(
            source,
            articles -> {
                getViewState().init(articles);
                getViewState().showLoading(false);
            });
    }

    void loadNewer(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, false);

        articlesInteractor.execute(bundle, response ->
                getViewState().addNewer(response.getArticles(), response.getIsRefresh()));
    }

    void loadOlder(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, true);

        getViewState().showLoading(true);
        articlesInteractor.execute(bundle, response -> {
            getViewState().addOlder(response.getArticles());
            getViewState().showLoading(false);
        });
    }

    void showArticle(Article article, Source source){
        getRouter().showArticleDetails(article, source);
    }
}
