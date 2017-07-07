package ua.in.zeusapps.ukrainenews.components.main.fragments.topStories;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.domain.GetLocalSourcesInteractor;
import ua.in.zeusapps.ukrainenews.domain.GetTopArticlesInteractor;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

@InjectViewState
public class TopStoriesPresenter extends MvpPresenter<TopStoriesView, MainRouter> {

    @Inject
    MainRouter _router;
    @Inject
    GetTopArticlesInteractor _topArticlesInteractor;
    @Inject
    GetLocalSourcesInteractor _localSourcesInteractor;

    TopStoriesPresenter(){
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return _router;
    }

    void showArticle(Article article){
        getRouter().showArticleDetails(article);
    }

    @Override
    protected void onFirstViewAttach() {
        _localSourcesInteractor.execute(sources -> {
            _topArticlesInteractor.execute(articles -> getViewState()
                    .showArticles(articles, sources));
        });
    }
}