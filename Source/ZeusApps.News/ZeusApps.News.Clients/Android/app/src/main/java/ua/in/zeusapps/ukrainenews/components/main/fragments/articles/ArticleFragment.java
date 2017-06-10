package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.EndlessRecyclerViewScrollListener;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article)
public class ArticleFragment
        extends BaseMainFragment
        implements  ArticleView {

    private static final String SOURCE_EXTRA = "source";
    private Subscription _subscription;
    private Source source;
    private RecyclerViewAdapter<Article> _adapter;
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

    public static ArticleFragment newInstance(Source source) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable(SOURCE_EXTRA, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        source = getArguments().getParcelable(SOURCE_EXTRA);

        if (!presenter.isInRestoreState(this)){
            presenter.init(source);
        }
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
    public void onDestroy() {
        super.onDestroy();

        _subscription.unsubscribe();
    }

    private void initAdapter(List<Article> articles){
        _adapter = new ArticleAdapter(getActivity(), formatter, source);
        _adapter.addAll(articles);
        _adapter.setAdsProvider(adsProvider);
        _subscription = _adapter.getItemClicked().subscribe(new Action1<Article>() {
            @Override
            public void call(Article article) {
                getRootActivity().resetAppBarLayoutState();
                getPresenter().showArticle(article, source);
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadOlder(source, _adapter.getLast());
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(_adapter);
        // TODO implement View.OnScrollChangeListener
        recyclerView.setOnScrollListener(listener);
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Article article = _adapter.getFirst();
                presenter.loadNewer(source, article);
            }
        });
    }

    @Override
    public String getTitle() {
        return source.getTitle();
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