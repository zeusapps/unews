package ua.in.zeusapps.ukrainenews.components.root;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface RootView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showHello();
}
