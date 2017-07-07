package ua.in.zeusapps.ukrainenews.components.details;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalArticleIdsInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetSourceInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetTopArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;

@InjectViewState
public class DetailsPresenter
    extends MvpPresenter<DetailsView, DetailsRouter> {

    private final static String TAG = DetailsPresenter.class.getSimpleName();
    private String _articleId = null;

    @Inject
    GetSourceInteractor _sourceInteractor;
    @Inject
    GetLocalArticleIdsInteractor localArticlesInteractor;
    @Inject
    GetTopArticlesInteractor _topArticlesInteractor;
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

    void init(final String articleId, String sourceId){
        Log.d(TAG, "DetailsPresenter init call");

        if (sourceId != null) {
            _sourceInteractor.execute(
                    source -> localArticlesInteractor.execute(
                        articleIds -> initViewState(articleIds, articleId), source.getKey()),
                    sourceId);
            return;
        }

        _topArticlesInteractor.execute(articles -> {
            List<String> articleIds = new ArrayList<>();
            for (Article article: articles) {
                articleIds.add(article.getId());
            }

            initViewState(articleIds, articleId);
        });

    }

    void currentArticleChanged(String articleId){
        Log.d(TAG, "DetailsPresenter currentArticleChanged, articleId = " + articleId);
        _articleId = articleId;
        getViewState().switchTo(articleId);
    }

    private void initViewState(List<String> articleIds, String articleId){
        getViewState().load(articleIds);
        if (_articleId != null && articleIds.contains(_articleId)){
            getViewState().switchTo(_articleId);
        } else {
            _articleId = articleId;
            getViewState().switchTo(articleId);
        }
    }
}
