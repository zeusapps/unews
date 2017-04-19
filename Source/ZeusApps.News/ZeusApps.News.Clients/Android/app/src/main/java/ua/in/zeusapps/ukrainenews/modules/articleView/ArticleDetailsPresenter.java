package ua.in.zeusapps.ukrainenews.modules.articleView;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.RootRouter;

@InjectViewState
public class ArticleDetailsPresenter
        extends MvpPresenter<ArticleDetailsView, RootRouter> {

    @Inject
    RootRouter router;

    ArticleDetailsPresenter(){
        getComponent().inject(this);
    }

    public void init(String articleId, Source source){

    }

    private void show(Article article){
//        if (_view == null){
//            return;
//        }
//
//        Source source = _repository.getSourceByKey(article.getSourceId());
//        if (source != null){
//            _view.showArticle(article, source);
//        }
    }

    @Override
    public RootRouter getRouter() {
        return router;
    }
}