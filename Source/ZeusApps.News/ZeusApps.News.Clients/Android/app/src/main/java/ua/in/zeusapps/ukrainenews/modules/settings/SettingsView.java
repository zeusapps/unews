package ua.in.zeusapps.ukrainenews.modules.settings;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;

public interface SettingsView extends BaseMainView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void articlesCleared();
}
