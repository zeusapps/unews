package ua.in.zeusapps.ukrainenews.components.details;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;


public interface DetailsView extends MvpView {
    void load(List<Article> articles, Source source);

    void switchTo(String id);
}
