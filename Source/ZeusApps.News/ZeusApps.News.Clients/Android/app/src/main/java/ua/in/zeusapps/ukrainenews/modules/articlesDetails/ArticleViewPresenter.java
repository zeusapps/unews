package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.root.RootRouter;

public class ArticleViewPresenter
        extends MvpPresenter<ArticleViewView, RootRouter> {

    @Inject
    RootRouter router;

    public ArticleViewPresenter() {
        getComponent().inject(this);
    }

    @Override
    public RootRouter getRouter() {
        return router;
    }
}
