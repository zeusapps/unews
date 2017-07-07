package ua.in.zeusapps.ukrainenews.components.main.fragments.topStories;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.components.articles.ArticleAdapter;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_top_stories)
public class TopStoriesFragment extends BaseMainFragment implements TopStoriesView {

    private Disposable _disposable;
    @InjectPresenter
    TopStoriesPresenter _presenter;
    @Inject
    Formatter _formatter;
    @BindView(R.id.fragment_top_articles_recyclerView)
    RecyclerView _recyclerView;

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public String getTitle() {
        return App.getInstance().getString(R.string.fragment_top_stories_title);
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
    public TopStoriesPresenter getPresenter() {
        return _presenter;
    }

    @Override
    public void showArticles(List<Article> articles, List<Source> sources) {
        ArticleAdapter adapter = new ArticleAdapter(
                getActivity(),
                _formatter,
                new ArticleAdapter.MultiSourceTitleSelector(sources),
                R.layout.fragment_article_item_template_small);
        adapter.addAll(articles);
        _disposable = adapter
                .getItemClicked()
                .subscribe(article -> getPresenter().showArticle(article));
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_disposable != null && !_disposable.isDisposed()) {
            _disposable.dispose();
        }
    }
}