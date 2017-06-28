package ua.in.zeusapps.ukrainenews.components.main.fragments.topStories;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.ArticleAdapter;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_top_stories)
public class TopStoriesFragment extends BaseMainFragment implements TopStoriesView {

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
        return getString(R.string.fragment_top_stories_title);
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
    public MvpPresenter getPresenter() {
        return _presenter;
    }

    @Override
    public void showArticles(List<Article> articles) {
        ArticleAdapter adapter = new ArticleAdapter(getActivity(), _formatter);
        adapter.addAll(articles);

        _recyclerView.setAdapter(adapter);
    }
}