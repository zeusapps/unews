package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.EndlessRecyclerViewScrollListener;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.services.Formatter;
import ua.in.zeusapps.ukrainenews.services.SettingsService;

@Layout(R.layout.fragment_article)
public class ArticleFragment
        extends BaseMainFragment
        implements  ArticleView {

    private static final String SCROLL_POSITION = "scroll_position";
    private static final String SOURCE_ID_EXTRA = "source_id";
    private static final String SOURCE_TITLE_EXTRA = "source_title";

    private LinearLayoutManager _layoutManager;
    private Source _source;
    private String _sourceTitle;
    private Disposable _disposable;
    private RecyclerViewAdapter<Article> _adapter;
    private int _scrollPosition = -1;

    @InjectPresenter
    ArticlePresenter presenter;
    @BindView(R.id.fragment_article_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_article_articlesRecyclerView)
    RecyclerView recyclerView;
    @Inject
    Formatter formatter;
    @Inject
    RecyclerViewAdapter.AdsProvider adsProvider;
    @Inject
    SettingsService settingsService;

    public static ArticleFragment newInstance(Source source) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(SOURCE_ID_EXTRA, source.getId());
        args.putString(SOURCE_TITLE_EXTRA, source.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sourceId = getArguments().getString(SOURCE_ID_EXTRA);
        _sourceTitle = getArguments().getString(SOURCE_TITLE_EXTRA);

        if (!presenter.isInRestoreState(this)){
            presenter.init(sourceId);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            _scrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SCROLL_POSITION, _layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public ArticlePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void init(List<Article> articles) {
        initAdapter(articles);
        initRecyclerView();
        initSwipeRefreshLayout();
    }

    @Override
    public void setSource(Source source) {
        _source = source;
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
        swipeRefreshLayout.setRefreshing(state);
    }

    @Override
    public void showLoadingError() {
        showError(getString(R.string.fragment_article_connection_error));
    }

    @Override
    public void showEmptyUpdate() {
        showInfo(getString(R.string.fragment_article_empty_update));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_disposable != null && !_disposable.isDisposed()) {
            _disposable.dispose();
        }
    }

    private void initAdapter(List<Article> articles){
        int resource = settingsService.getArticleTemplateType() == SettingsService.ARTICLE_TEMPLATE_BIG
                ? R.layout.fragment_article_item_template
                : R.layout.fragment_article_item_template_small;
        _adapter = new ArticleAdapter(getActivity(), formatter, _source, resource);
        _adapter.addAll(articles);
        _adapter.setAdsProvider(adsProvider);
        _disposable = _adapter.getItemClicked().subscribe(article -> {
            getRootActivity().resetAppBarLayoutState();
            getPresenter().showArticle(article, _source);
        });
    }

    private void initRecyclerView(){
        _layoutManager = new LinearLayoutManager(getContext());
        final EndlessRecyclerViewScrollListener listener =
                new EndlessRecyclerViewScrollListener(_layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadOlder(_source, _adapter.getLast());
            }
        };

        recyclerView.setLayoutManager(_layoutManager);
        recyclerView.setAdapter(_adapter);
        // TODO implement View.OnScrollChangeListener
        recyclerView.setOnScrollListener(listener);

        if (_scrollPosition != -1) {
            recyclerView.scrollToPosition(_scrollPosition);
        }
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Article article = _adapter.getFirst();
            presenter.loadNewer(_source, article);
        });
    }

    @Override
    public String getTitle() {
        return _sourceTitle;
    }

    @Override
    public int getFabButtonIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getFabButtonAction() {
        return null;
    }
}