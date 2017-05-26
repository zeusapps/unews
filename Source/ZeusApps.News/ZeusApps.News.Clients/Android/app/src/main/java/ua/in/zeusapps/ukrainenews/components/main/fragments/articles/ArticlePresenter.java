package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;


import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.domain.GetArticlesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetInitialArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.ArticleResponse;
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
                new LoaderSubscriber<List<Article>>() {
                    @Override
                    public void onNext(List<Article> articles) {
                        getViewState().init(articles);
                    }
                });
    }

    void loadNewer(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, false);

        articlesInteractor.execute(bundle, new LoaderSubscriber<ArticleResponse>() {
            @Override
            public void onNext(ArticleResponse response) {
                getViewState().addNewer(response.getArticles(), response.getIsRefresh());
            }
        });
    }

    void loadOlder(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, true);

        getViewState().showLoading(true);
        articlesInteractor.execute(bundle, new LoaderSubscriber<ArticleResponse>() {
            @Override
            public void onNext(ArticleResponse response) {
                getViewState().addOlder(response.getArticles());
            }
        });
    }

    void showArticle(Article article, Source source){
        getRouter().showArticleDetails(article, source);
    }

    private abstract class LoaderSubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {
            getViewState().showLoading(false);
        }

        @Override
        public void onError(Throwable e) {
            getViewState().showLoading(false);
            getViewState().showLoadingError();
        }
    }
}
