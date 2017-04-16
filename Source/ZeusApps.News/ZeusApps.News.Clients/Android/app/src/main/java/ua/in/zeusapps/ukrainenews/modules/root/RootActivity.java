package ua.in.zeusapps.ukrainenews.modules.root;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.helpers.FragmentHelper;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;
import ua.in.zeusapps.ukrainenews.models.Source;

@Layout(R.layout.activity_root)
public class RootActivity
        extends MvpActivity
        implements RootView {

    private final static int PERIOD_TO_CLOSE = 2000;
    private long _lastPressedTimestamp;
    @InjectPresenter
    RootPresenter presenter;

    @BindView(R.id.activity_root_content)
    FrameLayout rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_root_fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
    }

    @Override
    public void showMessage(String message) {
        NotificationHelper.showSnackbarInfoMessage(rootView, message);
    }

    @Override
    public void showSources() {
        //TODO implement BaseRootFragment
        //showFragment(new SourcesFragment());
    }

    @Override
    public void showArticles(Source source) {
        //TODO implement BaseRootFragment
        //showFragment(ArticleFragment.newInstance(source));
    }

    @Override
    public void onBackPressed() {
        int count = FragmentHelper.getStackCount(getSupportFragmentManager());

        if (count > 1){
            super.onBackPressed();
        } else {
            tryClose();
        }
    }

    public void resolveFab(BaseRootFragment fragment) {
        if (fragment.getFabButtonIcon() > 0) {
            fab.setImageResource(fragment.getFabButtonIcon());
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(fragment.getFabButtonAction());
        } else {
            fab.setVisibility(View.GONE);
            fab.setOnClickListener(null);
        }
    }

    public void resolveToolbar(BaseRootFragment fragment) {
        toolbar.setTitle(fragment.getTitle());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setNavigationOnClickListener(null);
        }
    }

    private void tryClose(){
        long timestamp = System.currentTimeMillis();
        if (timestamp - _lastPressedTimestamp < PERIOD_TO_CLOSE){
            finish();
            System.exit(0);
            return;
        }

        _lastPressedTimestamp = timestamp;
        showMessage(getString(R.string.root_activity_close_notification));
    }

    private void showFragment(BaseRootFragment fragment){
        FragmentHelper.replace(
                getSupportFragmentManager(),
                fragment,
                R.id.activity_root_content);
    }
}
