package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.main.MainRouter;

public class ArticleViewPresenter
        extends MvpPresenter<ArticleViewView, MainRouter> {

    @Inject
    MainRouter router;

    public ArticleViewPresenter() {
        getComponent().inject(this);
    }

    @Override
    public MainRouter getRouter() {
        return router;
    }
}
