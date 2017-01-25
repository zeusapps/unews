package ua.in.zeusapps.ukrainenews.modules.articles;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.ArticleAdapter;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;

public class ArticleFragment extends BaseFragment implements ArticleMVP.IView {

    private ArticleAdapter _adapter;

    @Inject
    ArticleMVP.IPresenter presenter;

    @BindView(R.id.fragment_article_articlesRecyclerView)
    RecyclerView recyclerView;


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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _adapter = new ArticleAdapter(getActivity());
        recyclerView.setAdapter(_adapter);

        presenter.updateArticles("korr");
    }

    @Override
    public void updateArticles(List<Article> sources) {
        _adapter.replaceAll(sources);
    }
}
