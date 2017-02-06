package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.articleView.ArticleViewFragment;
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;

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
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getFragments() != null){
            return;
        }

        manager
                .beginTransaction()
                .add(R.id.activity_main_content, new ArticleFragment(), ArticleFragment.TAG)
                .commit();
    }

    @Override
    public void onArticleSelected(Article article) {
        presenter.showArticle(article);
    }

    @Override
    public void switchToArticleView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_content,
                        new ArticleViewFragment(),
                        ArticleViewFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}