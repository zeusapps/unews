package ua.in.zeusapps.ukrainenews.modules.root;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import ua.in.zeusapps.ukrainenews.modules.articles.ArticleFragment;
import ua.in.zeusapps.ukrainenews.modules.settings.SettingsFragment;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesFragment;

@Layout(R.layout.activity_root)
public class RootActivity
        extends MvpActivity
        implements RootView, RootRouter {

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
    protected RootPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showMessage(String message) {
        NotificationHelper.showSnackbarInfoMessage(rootView, message);
    }

    @Override
    public void showSources() {
        FragmentHelper.clearStack(getSupportFragmentManager());
        FragmentHelper.replace(
                getSupportFragmentManager(),
                new SourcesFragment(),
                R.id.activity_root_content);
    }

    @Override
    public void showArticles(Source source) {
        showFragment(ArticleFragment.newInstance(source));
    }

    @Override
    public void showSettings() {
        showFragment(new SettingsFragment());
    }

    @Override
    public void onBackPressed() {
        int count = FragmentHelper.getStackCount(getSupportFragmentManager());

        if (count > 0){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_root_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_root_menu_settings){
            getPresenter().showSettings();
            return true;
        }

        return false;
    }

    private void showFragment(BaseRootFragment fragment){
        FragmentHelper.add(
                getSupportFragmentManager(),
                fragment,
                R.id.activity_root_content);
    }
}
