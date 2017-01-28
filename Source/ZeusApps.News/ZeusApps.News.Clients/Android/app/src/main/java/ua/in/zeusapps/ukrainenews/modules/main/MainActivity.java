package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
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

    private static final String ARTICLES_FRAGMENT_KEY = ArticleFragment.class.getSimpleName();
    private static final String ARTICLES_VIEW_FRAGMENT_KEY = ArticleViewFragment.class.getSimpleName();
    private static final String SOURCE_FRAGMENT_KEY = SourceFragment.class.getSimpleName();

    @BindView(R.id.activity_main_content)
    FrameLayout contentLayout;
    @BindView(R.id.activity_main_drawerLayout)
    DrawerLayout drawerLayout;

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

        if (manager.findFragmentByTag(SOURCE_FRAGMENT_KEY) != null){
            return;
        }

        manager
                .beginTransaction()
                .add(R.id.activity_main_sourceFragmentPlaceholder, new SourceFragment(), SOURCE_FRAGMENT_KEY)
                .add(R.id.activity_main_content, new ArticleFragment(), ARTICLES_FRAGMENT_KEY)
                .commit();
    }

    @Override
    public void onSourceChanged(Source source) {
        drawerLayout.closeDrawer(GravityCompat.START);
        presenter.updateSource(source);
    }

    @Override
    public void onArticleSelected(Article article) {
        presenter.updateArticle(article);
    }

    @Override
    public void switchToArticleView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_content, new ArticleViewFragment(), ARTICLES_VIEW_FRAGMENT_KEY)
                .addToBackStack(null)
                .commit();
    }
}