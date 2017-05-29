package ua.in.zeusapps.ukrainenews.components.details;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class DetailsPresenter
    extends MvpPresenter<DetailsView, DetailsRouter> {

    @Inject
    GetLocalArticlesInteractor localArticlesInteractor;
    @Inject
    DetailsRouter router;

    DetailsPresenter(){
        getComponent().inject(this);
    }

    @Override
    public DetailsRouter getRouter() {
        return router;
    }

    public void init(final Source source, final String articleId){
        localArticlesInteractor.execute(source, new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Article> articles) {
                getViewState().load(articles, source);
                getViewState().switchTo(articleId);
            }
        });

    }
}
