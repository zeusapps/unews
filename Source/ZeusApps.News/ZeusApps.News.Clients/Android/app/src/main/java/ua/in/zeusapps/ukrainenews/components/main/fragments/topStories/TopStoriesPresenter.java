package ua.in.zeusapps.ukrainenews.components.main.fragments.topStories;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;
import ua.in.zeusapps.ukrainenews.domain.GetTopArticlesInteractor;

public class TopStoriesPresenter extends MvpPresenter<TopStoriesView, MainRouter> {

    @Inject
    MainRouter _router;
    @Inject
    GetTopArticlesInteractor _topArticlesInteractor;

    @Override
    public MainRouter getRouter() {
        return _router;
    }

    @Override
    protected void onFirstViewAttach() {
        _topArticlesInteractor.execute(articles -> getViewState().showArticles(articles));
    }
}