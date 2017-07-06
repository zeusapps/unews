package ua.in.zeusapps.ukrainenews.components.articles;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ArticlesView extends MvpView {
    void init(List<Article> articles);
    void setSource(Source source);
    void addNewer(List<Article> articles, boolean refresh);
    void addOlder(List<Article> articles);
    void showLoading(boolean state);
    @StateStrategyType(SingleStateStrategy.class)
    void showLoadingError();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEmptyUpdate();
}
