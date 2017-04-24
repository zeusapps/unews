package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootView;

public interface ArticleDetailsView extends BaseRootView {
    void loadArticles(List<Article> articles);
}
