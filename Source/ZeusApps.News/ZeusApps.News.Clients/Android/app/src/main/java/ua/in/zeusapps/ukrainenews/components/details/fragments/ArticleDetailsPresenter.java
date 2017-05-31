package ua.in.zeusapps.ukrainenews.components.details.fragments;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.details.DetailsRouter;

public class ArticleDetailsPresenter
    extends MvpPresenter<ArticleDetailsView, DetailsRouter> {

    @Inject
    DetailsRouter router;

    ArticleDetailsPresenter() {
        getComponent().inject(this);
    }

    @Override
    public DetailsRouter getRouter() {
        return router;
    }

    public void close(){
        getRouter().close();
    }
}
