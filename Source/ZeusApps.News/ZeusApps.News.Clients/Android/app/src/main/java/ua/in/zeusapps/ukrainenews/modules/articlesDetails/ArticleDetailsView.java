package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;

public interface ArticleDetailsView extends BaseMainView {
    void loadArticles(List<Article> articles);
}
