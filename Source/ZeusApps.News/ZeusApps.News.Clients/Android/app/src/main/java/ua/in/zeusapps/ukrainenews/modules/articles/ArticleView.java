package ua.in.zeusapps.ukrainenews.modules.articles;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;

public interface ArticleView extends MvpView {
    void load(List<Article> articles);
    void addNewer(List<Article> articles);
    void addOlder(List<Article> articles);
    void showLoading(boolean state);
}
