package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.helpers.FragmentHelper;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesFragment;

public class MainActivity
        extends BaseActivity
        implements MainActivityMVP.View {

    private final static int PERIOD_TO_CLOSE = 2000;
    private long _lastPressedTimestamp;

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
        if (FragmentHelper.getStackCount(getSupportFragmentManager()) == 0){
            //showFragment(new ArticleFragment());
            FragmentHelper.add(getSupportFragmentManager(), new SourcesFragment(), R.id.activity_main_content);
        }
    }

    @Override
    public void switchToArticleView(Article article) {
        //showFragment(ArticleViewFragment.newInstance(article.getId()));
    }

    @Override
    public void switchToSettingsView() {
        //showFragment(new SettingsFragment());
    }

    @Override
    public void onBackPressed() {
        BaseFragment frg =  (BaseFragment) FragmentHelper
                .getVisibleFragment(getSupportFragmentManager());

//        if (!frg.getTag().equals(ArticleFragment.TAG)){
//            super.onBackPressed();
//            return;
//        }

        long timestamp = System.currentTimeMillis();
        if (timestamp - _lastPressedTimestamp < PERIOD_TO_CLOSE){
            finish();
            System.exit(0);
            return;
        }

        _lastPressedTimestamp = timestamp;
        Toast.makeText(this, R.string.root_activity_close_notification, Toast.LENGTH_SHORT).show();
    }

    private void showFragment(BaseFragment fragment){
        FragmentHelper.add(
                getSupportFragmentManager(),
                fragment,
                R.id.activity_main_content,
                fragment.getTag());
    }
}