package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;

public interface ArticlesDetailsView extends MvpView {
    void loadArticles(List<Article> articles);
    void switchToArticle(String articleId);
}