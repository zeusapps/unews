package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ArticleView extends BaseMainView {
    void init(List<Article> articles);
    void setSource(Source source);
    void addNewer(List<Article> articles, boolean refresh);
    void addOlder(List<Article> articles);
    void showLoading(boolean state);
    @StateStrategyType(SingleStateStrategy.class)
    void showLoadingError();
}
