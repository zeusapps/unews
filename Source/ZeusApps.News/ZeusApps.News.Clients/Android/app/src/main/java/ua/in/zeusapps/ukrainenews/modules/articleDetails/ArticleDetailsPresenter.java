package ua.in.zeusapps.ukrainenews.modules.articleDetails;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import rx.functions.Action1;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.RootRouter;

@InjectViewState
public class ArticleDetailsPresenter
        extends MvpPresenter<ArticleDetailsView, RootRouter> {

    @Inject
    RootRouter router;
    @Inject
    IArticleRepository articleRepository;

    ArticleDetailsPresenter(){
        getComponent().inject(this);
    }

    public void init(String articleId, final Source source){
        articleRepository.getById(articleId)
                .subscribe(new Action1<Article>() {
                    @Override
                    public void call(Article article) {
                        getViewState().showArticle(article, source);
                    }
                });
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