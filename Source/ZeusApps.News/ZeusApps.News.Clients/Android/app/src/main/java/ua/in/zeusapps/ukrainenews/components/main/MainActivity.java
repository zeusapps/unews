package ua.in.zeusapps.ukrainenews.components.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.HideToolbar;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.sources.SourcesFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.topStories.TopStoriesFragment;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;

@Layout(R.layout.activity_main)
public class MainActivity
        extends MvpActivity
        implements MainView, ViewPager.OnPageChangeListener {

    private final static int PERIOD_TO_CLOSE = 2000;
    private long _lastPressedTimestamp;
    @InjectPresenter
    MainPresenter _presenter;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @BindView(R.id.activity_root_fab)
    FloatingActionButton _fab;
    @BindView(R.id.activity_main_view_pager)
    ViewPager _viewPager;
    @BindView(R.id.activity_main_tab_layout)
    TabLayout _tabLayout;

    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        getPresenter().init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        _viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        _viewPager.removeOnPageChangeListener(this);
    }

    @Override
    protected MainPresenter getPresenter() {
        return _presenter;
    }

    @Override
    public void showHello() {
        showMessage(getString(R.string.activity_root_hello_message));
    }

    @Override
    public void init() {
        setSupportActionBar(_toolbar);
        setupViewPager();
        setupTabLayout();
    }

    @Override
    public void changePage(int position) {
        int current = _viewPager.getCurrentItem();

        if (position != current){
            _viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onBackPressed() {
        tryClose();
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

    public void resolveFab(BaseMainFragment fragment) {
        if (fragment.getFabButtonIcon() > 0) {
            _fab.setImageResource(fragment.getFabButtonIcon());
            _fab.setVisibility(View.VISIBLE);
            _fab.setOnClickListener(fragment.getFabButtonAction());
        } else {
            _fab.setVisibility(View.GONE);
            _fab.setOnClickListener(null);
        }
    }

    public void resolveToolbar(BaseMainFragment fragment) {
        if (fragment instanceof HideToolbar){
            _toolbar.setVisibility(View.GONE);
            return;
        } else {
            _toolbar.setVisibility(View.VISIBLE);
        }

        _toolbar.setTitle(fragment.getTitle());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            _toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
            _toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            _toolbar.setNavigationIcon(null);
            _toolbar.setNavigationOnClickListener(null);
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

    private void showMessage(String message){
        NotificationHelper.showSnackbarInfoMessage(_viewPager, message);
    }

    private void setupViewPager(){
        if (_viewPager.getAdapter() != null){
            return;
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.add(new TopStoriesFragment());
        adapter.add(new SourcesFragment());

        _viewPager.setAdapter(adapter);
    }

    private void setupTabLayout(){
        _tabLayout.setupWithViewPager(_viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getPresenter().pageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseMainFragment> _fragments = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return _fragments.get(position);
        }

        @Override
        public int getCount() {
            return _fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _fragments.get(position).getTitle();
        }

        void add(BaseMainFragment fragment){
            _fragments.add(fragment);
        }
    }
}
