package ua.in.zeusapps.ukrainenews.modules.articles;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootView;

public interface ArticleView extends BaseRootView {
    void init(List<Article> articles);
    void addNewer(List<Article> articles, boolean refresh);
    void addOlder(List<Article> articles);
    void showLoading(boolean state);
    @StateStrategyType(SingleStateStrategy.class)
    void showLoadingError();
}
