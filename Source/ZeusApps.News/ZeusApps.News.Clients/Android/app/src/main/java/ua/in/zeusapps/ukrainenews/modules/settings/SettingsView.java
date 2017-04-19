package ua.in.zeusapps.ukrainenews.modules.settings;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ua.in.zeusapps.ukrainenews.modules.root.BaseRootView;

public interface SettingsView extends BaseRootView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void articlesCleared();
}
