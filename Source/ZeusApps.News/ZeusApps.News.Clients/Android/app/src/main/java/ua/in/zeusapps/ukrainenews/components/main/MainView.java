package ua.in.zeusapps.ukrainenews.components.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showHello();
    void init();
    @StateStrategyType(SingleStateStrategy.class)
    void changePage(int position);
}
