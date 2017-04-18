package ua.in.zeusapps.ukrainenews.modules.articles;


import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.domain.GetArticlesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetInitialArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.ArticleRequestBundle;
import ua.in.zeusapps.ukrainenews.models.ArticleResponse;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.RootRouter;

@InjectViewState
public class ArticlePresenter extends MvpPresenterBase<ArticleView, RootRouter> {

    @Inject
    GetInitialArticlesInteractor initialArticlesInteractor;
    @Inject
    GetArticlesInteractor articlesInteractor;

    ArticlePresenter() {
        getComponent().inject(this);
    }

    void init(Source source){
        initialArticlesInteractor.execute(
                source,
                new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showLoadingError();
                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        getViewState().init(articles);
                    }
                });
    }

    void loadNewer(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, false);

        articlesInteractor.execute(bundle, new Subscriber<ArticleResponse>() {
            @Override
            public void onCompleted() {
                getViewState().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                getViewState().showLoadingError();
            }

            @Override
            public void onNext(ArticleResponse response) {
                getViewState().addNewer(response.getArticles(), response.getIsRefresh());
            }
        });
    }

    void loadOlder(Source source, Article article) {
        ArticleRequestBundle bundle = new ArticleRequestBundle(source, article, true);

        getViewState().showLoading(true);
        articlesInteractor.execute(bundle, new Subscriber<ArticleResponse>() {
            @Override
            public void onCompleted() {
                getViewState().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                getViewState().showLoadingError();
            }

            @Override
            public void onNext(ArticleResponse response) {
                getViewState().addOlder(response.getArticles());
            }
        });
    }
}
