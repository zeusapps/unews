package ua.in.zeusapps.ukrainenews.modules.sources;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;

public interface SourcesView extends MvpView {
    void showSources(List<Source> sources);

    void showMessage(String message);
}
