package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.RootRouter;

@InjectViewState
public class ArticleDetailsPresenter
        extends MvpPresenter<ArticleDetailsView, RootRouter> {

    @Inject
    RootRouter router;
    @Inject
    GetLocalArticlesInteractor localArticlesInteractor;

    ArticleDetailsPresenter(){
        getComponent().inject(this);
    }

    void init(final Source source){
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
            }
        });
    }

    @Override
    public RootRouter getRouter() {
        return router;
    }
}