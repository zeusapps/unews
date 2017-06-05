package ua.in.zeusapps.ukrainenews.components.details;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;


public interface DetailsView extends MvpView {
    void load(List<String> articleIds, Source source);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void switchTo(String id);
}
