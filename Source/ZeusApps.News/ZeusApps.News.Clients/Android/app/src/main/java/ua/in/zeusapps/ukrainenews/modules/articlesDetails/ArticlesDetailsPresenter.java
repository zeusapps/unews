package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.root.RootRouter;

public class ArticlesDetailsPresenter
    extends MvpPresenter<ArticlesDetailsView, RootRouter>{

    @Inject
    RootRouter rootRouter;
    @Inject
    GetLocalArticlesInteractor localArticlesInteractor;


    ArticlesDetailsPresenter(){
        getComponent().inject(this);
    }

    void init(final Source source, final String articleId){
        localArticlesInteractor.execute(source, new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Article> articles) {
                if (articles.size() == 0){
                    getRouter().showArticles(source);
                }

                getViewState().loadArticles(articles);
                getViewState().switchToArticle(articleId);
            }
        });
    }

    @Override
    public RootRouter getRouter() {
        return rootRouter;
    }
}
