package ua.in.zeusapps.ukrainenews.components.details;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalArticleIdsInteractor;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class DetailsPresenter
    extends MvpPresenter<DetailsView, DetailsRouter> {

    private final static String TAG = DetailsPresenter.class.getSimpleName();
    private String _articleId = null;

    @Inject
    GetLocalArticleIdsInteractor localArticlesInteractor;
    @Inject
    DetailsRouter router;

    DetailsPresenter(){
        Log.d(TAG, "DetailsPresenter constructor call");
        getComponent().inject(this);
    }

    @Override
    public DetailsRouter getRouter() {
        return router;
    }

    void init(final Source source, final String articleId){
        Log.d(TAG, "DetailsPresenter init call");
        localArticlesInteractor.execute(source, new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> articleIds) {
                getViewState().load(articleIds, source);

                if (_articleId != null && articleIds.contains(_articleId)){
                    getViewState().switchTo(_articleId);
                } else {
                    _articleId = articleId;
                    getViewState().switchTo(articleId);
                }
            }
        });

    }

    void currentArticleChanged(String articleId){
        Log.d(TAG, "DetailsPresenter currentArticleChanged, articleId = " + articleId);
        _articleId = articleId;
        getViewState().switchTo(articleId);
    }
}
