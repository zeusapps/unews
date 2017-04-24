package ua.in.zeusapps.ukrainenews.modules.main;

import ua.in.zeusapps.ukrainenews.models.Article;

class MainActivityPresenter implements MainActivityMVP.Presenter {

    private MainActivityMVP.View _view;

    @Override
    public void setView(MainActivityMVP.View view) {
        _view = view;
    }

    @Override
    public void showArticle(Article article) {
        if (article != null){
            _view.switchToArticleView(article);
        }
    }
}
