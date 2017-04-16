package ua.in.zeusapps.ukrainenews.modules.articles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.EndlessRecyclerViewScrollListener;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpPresenterBase;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootFragment;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article)
public class ArticleFragment
        extends BaseRootFragment
        implements  ArticleView {

    private static final String SOURCE_EXTRA = "source";
    private Source source;
    private RecyclerViewAdapter<Article> adapter;
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
    public MvpPresenterBase getPresenter() {
        return presenter;
    }

    @Override
    public void init(List<Article> articles) {
        initAdapter(articles);
        initRecyclerView();
        initSwipeRefreshLayout();
    }

    @Override
    public void addNewer(List<Article> articles) {
        // TODO add newer
    }

    @Override
    public void addOlder(List<Article> articles) {
        // TODO add older
    }

    @Override
    public void showLoading(boolean state) {
        swipeRefreshLayout.setRefreshing(state);
    }

    private void initAdapter(List<Article> articles){
        adapter = new ArticleAdapter(getActivity(), formatter);
        adapter.addAll(articles);
        //adapter.setAdsProvider(adsProvider);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadOlder(source, adapter.getLast());
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // TODO implement View.OnScrollChangeListener
        recyclerView.setOnScrollListener(listener);
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Article article = adapter.getFirst();
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