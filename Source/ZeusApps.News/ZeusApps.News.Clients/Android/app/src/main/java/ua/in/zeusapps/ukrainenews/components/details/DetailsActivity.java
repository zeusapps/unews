package ua.in.zeusapps.ukrainenews.components.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;

@Layout(R.layout.activity_details)
public class DetailsActivity
        extends MvpActivity
        implements DetailsView, ViewPager.OnPageChangeListener {

    public static final String TAG = DetailsActivity.class.getSimpleName();

    public static final String ARTICLE_ID_EXTRA = "article_id_extra";

    public static final String SOURCE_ID_EXTRA = "source_id_extra";

    private DetailsAdapter _adapter;

    @InjectPresenter
    DetailsPresenter presenter;
    @BindView(R.id.activity_details_viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        super.onCreateOverride(savedInstanceState);

        viewPager.addOnPageChangeListener(this);

        String articleId = getIntent().getStringExtra(ARTICLE_ID_EXTRA);
        String sourceId = getIntent().getStringExtra(SOURCE_ID_EXTRA);

        getPresenter().init(articleId, sourceId);
    }

    @Override
    protected DetailsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void load(List<String> articleIds) {
        Log.d(TAG, "Article IDs loaded");

        _adapter = new DetailsAdapter(getSupportFragmentManager(), articleIds);
        viewPager.setAdapter(_adapter);
    }

    @Override
    public void switchTo(String id) {
        int index = _adapter.find(id);

        if (index != -1 && index != viewPager.getCurrentItem()){
            Log.d(TAG, "ViewPager switching to: " + index);
            viewPager.setCurrentItem(index);
        } else {
            Log.d(TAG, "ViewPager switching ignored, index: " + index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String articleId = _adapter.getArticleId(position);

        Log.d(TAG, "Page selected.ID: " + articleId);
        getPresenter().currentArticleChanged(articleId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}