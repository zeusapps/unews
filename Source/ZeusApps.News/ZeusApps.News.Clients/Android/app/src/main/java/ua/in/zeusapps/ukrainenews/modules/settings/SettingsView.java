package ua.in.zeusapps.ukrainenews.modules.settings;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ua.in.zeusapps.ukrainenews.components.root.base.BaseRootView;

public interface SettingsView extends BaseRootView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void articlesCleared();
}
