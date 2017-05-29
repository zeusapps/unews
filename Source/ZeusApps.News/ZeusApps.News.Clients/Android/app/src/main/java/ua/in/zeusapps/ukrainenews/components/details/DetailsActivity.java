package ua.in.zeusapps.ukrainenews.components.details;

import android.os.Bundle;
import android.provider.SyncStateContract;
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

@Layout(R.layout.activity_details)
public class DetailsActivity
        extends MvpActivity
        implements DetailsView {

    public static final String ARTICLE_ID_EXTRA = "article_id_extra";
    public static final String SOURCE_EXTRA = "source_extra";

    private DetailsAdapter _adapter;

    @InjectPresenter
    DetailsPresenter presenter;
    @BindView(R.id.activity_details_viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        super.onCreateOverride(savedInstanceState);

        String articleId = getIntent().getStringExtra(ARTICLE_ID_EXTRA);
        Source source = getIntent().getParcelableExtra(SOURCE_EXTRA);

        getPresenter().init(source, articleId);
    }

    @Override
    protected DetailsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void load(List<Article> articles, Source source) {
        _adapter = new DetailsAdapter(getSupportFragmentManager(), articles, source);
        viewPager.setAdapter(_adapter);
    }

    @Override
    public void switchTo(String id) {
        int index = _adapter.find(id);

        if (index != -1 && index != viewPager.getCurrentItem()){
            viewPager.setCurrentItem(index);
        }
    }
}