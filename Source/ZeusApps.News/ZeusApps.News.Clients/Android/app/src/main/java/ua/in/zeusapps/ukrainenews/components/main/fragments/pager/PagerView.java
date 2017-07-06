package ua.in.zeusapps.ukrainenews.components.main.fragments.pager;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainView;

public interface PagerView extends BaseMainView {
    void init();

    @StateStrategyType(SingleStateStrategy.class)
    void changePage(int position);
}
