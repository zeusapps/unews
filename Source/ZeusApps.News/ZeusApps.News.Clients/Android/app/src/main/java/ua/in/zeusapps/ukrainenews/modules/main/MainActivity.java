package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.common.FragmentHelper;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsFragment;

public class MainActivity
        extends BaseActivity
        implements MainActivityMVP.View,
            ArticleFragment.OnArticleSelectedListener {

    @BindView(R.id.activity_main_content)
    FrameLayout contentLayout;

    @Inject
    MainActivityMVP.Presenter presenter;

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
        FragmentHelper.replace(
                getSupportFragmentManager(),
                new ArticleFragment(),
                R.id.activity_main_content,
                ArticleFragment.TAG);
    }

    @Override
    public void onArticleSelected(Article article) {
        presenter.showArticle(article);
    }

    @Override
    public void switchToArticleView() {
        FragmentHelper.add(
                getSupportFragmentManager(),
                new ArticleViewFragment(),
                R.id.activity_main_content,
                ArticleViewFragment.TAG);
    }

    @Override
    public void switchToSettingsView() {
        FragmentHelper.add(
                getSupportFragmentManager(),
                new SettingsFragment(),
                R.id.activity_main_content,
                SettingsFragment.TAG);
    }
}