package ua.in.zeusapps.ukrainenews.modules.articleView;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootView;

public interface ArticleDetailsView extends BaseRootView {
    void showArticle(Article article, Source source);
}
