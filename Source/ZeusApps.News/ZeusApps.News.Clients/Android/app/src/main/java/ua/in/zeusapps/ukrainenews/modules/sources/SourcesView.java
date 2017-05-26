package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;

interface SourcesView extends BaseMainView {
    void showSources(List<Source> sources);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError();
}