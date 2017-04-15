package ua.in.zeusapps.ukrainenews.modules.articles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.ArticleAdapter;
import ua.in.zeusapps.ukrainenews.adapter.BaseAdsAdapter;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article)
public class ArticleFragment
        extends MvpFragment
        implements  ArticleView {

    @InjectPresenter
    ArticlePresenter presenter;

    @BindView(R.id.fragment_article_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_article_articlesRecyclerView)
    RecyclerView recyclerView;
    @Inject
    Formatter formatter;
    @Inject
    BaseAdsAdapter.AdsProvider adsProvider;

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void load(List<Article> articles) {
        initRecyclerView(articles);




    }

    @Override
    public void addNewer(List<Article> articles) {

    }

    @Override
    public void addOlder(List<Article> articles) {

    }

    @Override
    public void showLoading(boolean state) {

    }

    private void initRecyclerView(List<Article> articles){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArticleAdapter adapter = new ArticleAdapter(getActivity(), formatter);
        adapter.addAll(articles);

        adapter.addAdsProvider(new ArticleAds);


    }


//        extends BaseFragment
//        implements ArticleMVP.IView,
//            BaseAdapter.OnItemClickListener<Article>, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
//
//    public static final String TAG = ArticleFragment.class.getSimpleName();
//
//    private OnArticleSelectedListener _listener;
//    private ArticleAdapter _articleAdapter;
//    private List<Source> _sources;
//    private SubMenu _subMenu;
//
//    @Inject
//    ArticleMVP.IPresenter presenter;
//    @Inject
//    Formatter formatter;
//
//    @BindView(R.id.fragment_article_articlesRecyclerView)
//    RecyclerView articlesRecycleView;
//    @BindView(R.id.fragment_article_navigationView)
//    NavigationView navigationView;
//    @BindView(R.id.fragment_article_swipeRefreshLayout)
//    SwipeRefreshLayout refreshLayout;
//    @BindView(R.id.include_toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.fragment_article_drawerLayout)
//    DrawerLayout drawerLayout;
//
//    @Override
//    protected BaseMVP.IPresenter getPresenter() {
//        return presenter;
//    }
//
//    @Override
//    protected int getContentResourceId() {
//        return R.layout.fragment_article;
//    }
//
//    @Override
//    protected void inject(ApplicationComponent component) {
//        component.inject(this);
//    }
//
//    @Override
//    protected void onCreateViewOverride(View view, @Nullable Bundle savedInstanceState) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        final EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                presenter.loadOlder(_articleAdapter.getLast());
//            }
//        };
//        _subMenu = navigationView
//                .getMenu()
//                .addSubMenu(getResources().getString(R.string.sources));
//        _subMenu.setGroupCheckable(0, true, true);
//
//        _articleAdapter = new ArticleAdapter(getActivity(), formatter);
//        _articleAdapter.setOnItemClickListener(this);
//        _articleAdapter.addAdsProvider(new ArticleAdsProvider());
//
//        articlesRecycleView.setLayoutManager(layoutManager);
//        articlesRecycleView.addOnScrollListener(listener);
//        articlesRecycleView.setAdapter(_articleAdapter);
//
//        navigationView.setNavigationItemSelectedListener(this);
//
//        refreshLayout.setOnRefreshListener(this);
//
//        addToolBar();
//
//        presenter.initLoad();
//    }
//
//    @Override
//    public void updateArticles(List<Article> articles) {
//        _articleAdapter.replaceAll(articles);
//    }
//
//    @Override
//    public void addNewerArticles(List<Article> articles) {
//        _articleAdapter.addAll(articles, 0);
//    }
//
//    @Override
//    public void addOlderArticles(List<Article> articles) {
//        _articleAdapter.addAll(articles);
//    }
//
//    @Override
//    public void updateSources(List<Source> sources) {
//        _sources = sources;
//
//        Source source = presenter.getSelectedSource();
//        if (source == null && sources.size() > 0){
//            source = sources.get(0);
//        }
//
//        _subMenu.clear();
//
//        int index = 0;
//        for (Source src: sources){
//            MenuItem item = _subMenu.add(0, index, 0, src.getTitle());
//            if (src.equals(source)){
//                item.setChecked(true);
//                toolbar.setTitle(src.getTitle());
//            }
//            index++;
//        }
//    }
//
//    @Override
//    public void showError() {
//        Snackbar snackbar = Snackbar
//                .make(refreshLayout, getResources().getText(R.string.network_error), Snackbar.LENGTH_LONG);
//        snackbar.getView().setBackgroundColor(
//                ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
//        snackbar.show();
//    }
//
//    @Override
//    public void loadStarted() {
//        refreshLayout.setRefreshing(true);
//    }
//
//    @Override
//    public void loadComplete() {
//        refreshLayout.setRefreshing(false);
//    }
//
//    @Override
//    public void onItemClick(Article article) {
//        if (_listener == null){
//            return;
//        }
//
//        presenter.showArticle();
//        _listener.onArticleSelected(article);
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        drawerLayout.closeDrawer(GravityCompat.START);
//        if (item.getItemId() == R.id.fragment_article_menu_settings){
//            MainActivityMVP.View view = (MainActivityMVP.View) getActivity();
//            if (view == null){
//                return false;
//            }
//
//            view.switchToSettingsView();
//            return true;
//        }
//
//
//        for (int i = 0; i < _subMenu.size(); i++){
//            _subMenu.getItem(i).setChecked(false);
//        }
//
//        item.setChecked(true);
//
//        int index = item.getItemId();
//        if (index >= _sources.size()){
//            return false;
//        }
//
//        articlesRecycleView.scrollToPosition(0);
//        Source source = _sources.get(index);
//        presenter.setSelectedSource(source);
//        toolbar.setTitle(source.getTitle());
//        return true;
//    }
//
//    @Override
//    public void onRefresh() {
//        presenter.loadNewer(_articleAdapter.getFirst());
//    }
//
//    @Override
//    public String GetTag() {
//        return TAG;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof OnArticleSelectedListener) {
//            _listener = (OnArticleSelectedListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnArticleSelectedListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        _listener = null;
//    }
//
//    private void addToolBar(){
//        AppCompatActivity activity = getCompatActivity();
//        activity.setSupportActionBar(toolbar);
//
//        final ActionBar actionBar = getCompatActivity().getSupportActionBar();
//
//        if (actionBar != null)
//        {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    getActivity(),
//                    drawerLayout,
//                    toolbar,
//                    R.string.app_name,
//                    R.string.app_name);
//            toggle.setDrawerIndicatorEnabled(true);
//            drawerLayout.addDrawerListener(toggle);
//            toggle.syncState();
//        }
//    }
//
//    public interface OnArticleSelectedListener{
//        void onArticleSelected(Article article);
//    }
//
//    private class ArticleAdsProvider implements BaseAdsAdapter.AdsProvider{
//        @Override
//        public int getAdsOffset() {
//            return 3;
//        }
//
//        @Override
//        public int getAdsPeriod() {
//            return 5;
//        }
//
//        @Override
//        public int getAdTypeAtPosition(int position) {
//            return 0;
//        }
//
//        @Override
//        public BaseViewHolder getViewHolder(LayoutInflater inflater, ViewGroup parent, int adType) {
//            View view = inflater.inflate(R.layout.fragment_article_ads_template, parent, false);
//            return new AdHolder(view);
//        }
//
//        @Override
//        public void bindAdAtPosition(BaseViewHolder holder, int position) {
//            holder.update(getContext(), null);
//        }
//    }
}