package ua.in.zeusapps.ukrainenews.modules.root;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ua.in.zeusapps.ukrainenews.models.Source;

public interface RootView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    void showSources();

    void showArticles(Source source);
}
