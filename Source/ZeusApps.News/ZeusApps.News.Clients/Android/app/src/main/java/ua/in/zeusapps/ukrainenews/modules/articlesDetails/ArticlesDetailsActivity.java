package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

@Layout(R.layout.activity_articles_details)
public class ArticlesDetailsActivity
    extends MvpActivity
    implements ArticlesDetailsView{

    public static final String ARTICLE_ID_EXTRA = "article_id_extra";
    public static final String SOURCE_EXTRA = "source_extra";

    private Source _source;
    private ArticleAdapter _adapter;

    @BindView(R.id.activity_articles_details_viewPager)
    ViewPager viewPager;
    @InjectPresenter
    ArticlesDetailsPresenter presenter;

    @Override
    protected ArticlesDetailsPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        String articleId = getIntent().getStringExtra(ARTICLE_ID_EXTRA);
        _source = getIntent().getParcelableExtra(SOURCE_EXTRA);

        getPresenter().init(_source, articleId);
    }

    @Override
    public void loadArticles(List<Article> articles) {
        _adapter = new ArticleAdapter(getSupportFragmentManager(), articles, _source);
        viewPager.setAdapter(_adapter);
    }

    @Override
    public void switchToArticle(String articleId) {
        int position = _adapter.find(articleId);
        if (position != -1){
            viewPager.setCurrentItem(position);
        }
    }
}
