package ua.in.zeusapps.ukrainenews.components.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.EndlessRecyclerViewScrollListener;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.SettingsService;

@Layout(R.layout.activity_articles)
public class ArticlesActivity extends MvpActivity implements ArticlesView {

    public static final String SOURCE_ID_EXTRA = "source_id";
    private static final String SCROLL_POSITION = "scroll_position";

    private LinearLayoutManager _layoutManager = new LinearLayoutManager(this);
    private Source _source;
    private Disposable _disposable;
    private RecyclerViewAdapter<Article> _adapter;
    private int _scrollPosition = -1;

    @InjectPresenter
    ArticlesPresenter _presenter;
    @BindView(R.id.activity_articles_swipeRefreshLayout)
    SwipeRefreshLayout _swipeRefreshLayout;
    @BindView(R.id.activity_articles_articlesRecyclerView)
    RecyclerView _recyclerView;
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @Inject
    Formatter formatter;
    @Inject
    RecyclerViewAdapter.AdsProvider adsProvider;
    @Inject
    SettingsService settingsService;

    @Override
    protected ArticlesPresenter getPresenter() {
        return _presenter;
    }

    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        String sourceId = getIntent().getStringExtra(SOURCE_ID_EXTRA);

        if (!_presenter.isInRestoreState(this)){
            _presenter.init(sourceId);
        }

        if (savedInstanceState != null){
            _scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SCROLL_POSITION, _layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();

        _toolbar.setNavigationOnClickListener(v -> finish());
        _swipeRefreshLayout.setOnRefreshListener(() -> {
            Article article = _adapter.getFirst();
            if (article != null){
                getPresenter().loadNewer(_source, article);
            }
        });

        // TODO implement View.OnScrollChangeListener
        _recyclerView.setOnScrollListener(new EndlessRecyclerViewScrollListener(_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPresenter().loadOlder(_source, _adapter.getLast());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        _toolbar.setNavigationOnClickListener(null);
        _swipeRefreshLayout.setOnRefreshListener(null);
        _recyclerView.setOnScrollListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_disposable != null && !_disposable.isDisposed()){
            _disposable.dispose();
        }
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void init(List<Article> articles) {
        initAdapter(articles);
        initRecyclerView();
        initToolbar();
    }

    @Override
    public void setSource(Source source) {
        _source = source;
        _toolbar.setTitle(source.getTitle());
    }

    @Override
    public void addNewer(List<Article> articles, boolean refresh) {
        if (refresh){
            _adapter.clear();
        }

        _adapter.addAll(0, articles);
    }

    @Override
    public void addOlder(List<Article> articles) {
        _adapter.addAll(_adapter.getAll().size(), articles);
    }

    @Override
    public void showLoading(boolean state) {
        _swipeRefreshLayout.setRefreshing(state);
    }

    @Override
    public void showLoadingError() {
        NotificationHelper.showSnackbarErrorMessage(
                _recyclerView,
                getString(R.string.fragment_article_connection_error));
    }

    @Override
    public void showEmptyUpdate() {
        NotificationHelper.showSnackbarInfoMessage(
                _recyclerView,
                getString(R.string.fragment_article_empty_update));
    }

    private void initAdapter(List<Article> articles){
        int resource = settingsService.getArticleTemplateType() == SettingsService.ARTICLE_TEMPLATE_BIG
                ? R.layout.fragment_article_item_template
                : R.layout.fragment_article_item_template_small;




        _adapter = new ArticleAdapter(this, formatter, article -> _source.getTitle(), resource);
        _adapter.addAll(articles);
        _adapter.setAdsProvider(adsProvider);
        _disposable = _adapter.getItemClicked().subscribe(article -> getPresenter().showArticle(article, _source));
    }

    private void initRecyclerView(){
        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setAdapter(_adapter);
        if (_scrollPosition != -1) {
            _recyclerView.scrollToPosition(_scrollPosition);
        }
    }

    private void initToolbar(){
        _toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
    }
}
