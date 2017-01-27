package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewFragment;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewMVP;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleMVP;
import ua.in.zeusapps.ukrainenews.modules.source.SourceFragment;

public class MainActivity
        extends BaseActivity
        implements MainActivityMVP.View,
            SourceFragment.OnSelectedSourceChangedListener,
            ArticleFragment.OnArticleSelectedListener {

    private Source _selectedSource;

    @Inject
    MainActivityMVP.Presenter presenter;

    @Inject
    ArticleMVP.IPresenter articlePresenter;

    @Inject
    ArticleViewMVP.IPresenter articleViewPresenter;

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @NonNull
    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }


    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_sourceFragmentPlaceholder, new SourceFragment())
                .add(R.id.activity_main_content, new ArticleFragment())
                .commit();
    }

    @Override
    public void onSourceChanged(Source source) {
        if (source == null){
            return;
        }

        _selectedSource = source;
        articlePresenter.updateArticles(source.getKey());
    }

    @Override
    public void onArticleSelected(Article article) {
        if (article == null && _selectedSource == null){
            return;
        }

        articleViewPresenter.showArticle(article, _selectedSource);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_content, new ArticleViewFragment())
                .addToBackStack(null)
                .commit();
    }
}