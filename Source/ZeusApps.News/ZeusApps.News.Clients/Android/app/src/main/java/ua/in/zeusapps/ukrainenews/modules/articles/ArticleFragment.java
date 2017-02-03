package ua.in.zeusapps.ukrainenews.modules.articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.ArticleAdapter;
import ua.in.zeusapps.ukrainenews.adapter.BaseAdapter;
import ua.in.zeusapps.ukrainenews.adapter.EndlessRecyclerViewScrollListener;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleFragment
        extends BaseFragment
        implements ArticleMVP.IView,
            BaseAdapter.OnItemClickListener<Article>, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = ArticleFragment.class.getSimpleName();

    private OnArticleSelectedListener _listener;
    private ArticleAdapter _articleAdapter;
    private List<Source> _sources;

    @Inject
    ArticleMVP.IPresenter presenter;
    @Inject
    Formatter formatter;

    @BindView(R.id.fragment_article_articlesRecyclerView)
    RecyclerView articlesRecycleView;
    @BindView(R.id.fragment_article_navigationView)
    NavigationView navigationView;
    @BindView(R.id.fragment_article_swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.fragment_article_drawerLayout)
    DrawerLayout drawerLayout;

    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_article;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreateViewOverride(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadOlder(_articleAdapter.getLast());
            }
        };
        _articleAdapter = new ArticleAdapter(getActivity(), formatter);
        _articleAdapter.setOnItemClickListener(this);

        articlesRecycleView.setLayoutManager(layoutManager);
        articlesRecycleView.addOnScrollListener(listener);
        articlesRecycleView.setAdapter(_articleAdapter);

        navigationView.setNavigationItemSelectedListener(this);

        refreshLayout.setOnRefreshListener(this);

        presenter.initLoad();
    }

    @Override
    public void updateArticles(List<Article> articles) {
        _articleAdapter.replaceAll(articles);
    }

    @Override
    public void addNewerArticles(List<Article> articles) {
        _articleAdapter.addAll(articles, 0);
    }

    @Override
    public void addOlderArticles(List<Article> articles) {
        _articleAdapter.addAll(articles);
    }

    @Override
    public void updateSources(List<Source> sources) {
        _sources = sources;
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < sources.size(); i++){
            if (menu.findItem(i) == null){
                menu.add(R.id.fragment_article_menu_sources, i, Menu.FIRST, sources.get(i).getTitle());
            }
        }
    }

    @Override
    public void setChecked(String id) {
        for(int i = 0; i < _sources.size(); i++){
            boolean flag = _sources.get(i).getId().equals(id);
            navigationView
                    .getMenu()
                    .getItem(i)
                    .setChecked(flag);
        }
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar
                .make(refreshLayout, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark));
        snackbar.show();
    }

    @Override
    public void loadStarted() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void loadComplete() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(Article article) {
        if (_listener == null){
            return;
        }

        presenter.showArticle();
        _listener.onArticleSelected(presenter.getSelectedSource(), article);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int index = item.getItemId();
        if (index >= _sources.size()){
            return false;
        }

        Source source = _sources.get(index);
        presenter.setSelectedSource(source);
        articlesRecycleView.scrollTo(0,0);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        presenter.loadNewer(_articleAdapter.getFirst());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnArticleSelectedListener) {
            _listener = (OnArticleSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    public interface OnArticleSelectedListener{
        void onArticleSelected(Source source, Article article);
    }
}