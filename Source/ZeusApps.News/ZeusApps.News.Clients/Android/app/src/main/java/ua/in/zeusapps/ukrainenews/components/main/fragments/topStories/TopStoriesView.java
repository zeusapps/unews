package ua.in.zeusapps.ukrainenews.components.main.fragments.topStories;

import java.util.List;

import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;
import ua.in.zeusapps.ukrainenews.models.Article;

public interface TopStoriesView
    extends BaseMainView {

    void showArticles(List<Article> articles);
}
