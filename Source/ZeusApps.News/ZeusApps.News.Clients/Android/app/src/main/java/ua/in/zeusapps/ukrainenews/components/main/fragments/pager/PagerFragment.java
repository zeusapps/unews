package ua.in.zeusapps.ukrainenews.components.main.fragments.pager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.sources.SourcesFragment;
import ua.in.zeusapps.ukrainenews.components.main.fragments.topStories.TopStoriesFragment;

@Layout(R.layout.fragment_pager)
public class PagerFragment
        extends BaseMainFragment
        implements PagerView, ViewPager.OnPageChangeListener {

    @InjectPresenter
    PagerPresenter _pagerPresenter;
    @BindView(R.id.fragment_pager_viewPager)
    ViewPager _viewPager;
    @BindView(R.id.fragment_pager_tabLayout)
    TabLayout _tabLayout;

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public int getFabButtonIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getFabButtonAction() {
        return null;
    }

    @Override
    public PagerPresenter getPresenter() {
        return _pagerPresenter;
    }

    @Override
    public void init() {
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
    public void onResume() {
        super.onResume();

        getPresenter().init();
        _viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        _viewPager.removeOnPageChangeListener(this);
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

    private void setupViewPager(){
        if (_viewPager.getAdapter() != null){
            return;
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.add(new TopStoriesFragment());
        adapter.add(new SourcesFragment());

        _viewPager.setAdapter(adapter);
    }

    private void setupTabLayout(){
        _tabLayout.setupWithViewPager(_viewPager);
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
