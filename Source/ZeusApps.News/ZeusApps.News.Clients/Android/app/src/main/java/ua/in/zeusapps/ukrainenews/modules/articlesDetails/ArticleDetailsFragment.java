package ua.in.zeusapps.ukrainenews.modules.articlesDetails;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;

class ArticleAdapter extends FragmentPagerAdapter {
    private final List<Article> _articles;
    private final Source _source;

    ArticleAdapter(
            FragmentManager manager,
            List<Article> articles,
            Source source) {
        super(manager);

        _articles = articles;
        _source = source;
    }

    int find(String id){
        for (int i = 0; i < _articles.size(); i++){
            if (_articles.get(i).getId().equals(id)){
                return i;
            }
        }

        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleViewFragment.newInstance(_articles.get(position), _source);
    }

    @Override
    public int getCount() {
        return _articles.size();
    }
}

@Layout(R.layout.fragment_article_details)
public class ArticleDetailsFragment
        extends BaseMainFragment
        implements ArticleDetailsView {

    private static final String ARTICLE_ID_EXTRA = "article_id";
    private static final String SOURCE_EXTRA = "source";

    private Source _source;
    private String _articleId;

    @BindView(R.id.fragment_article_details_viewPager)
    ViewPager viewPager;
    @InjectPresenter
    ArticleDetailsPresenter presenter;

    public static ArticleDetailsFragment newInstance(Source source, String articleId) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID_EXTRA, articleId);
        args.putParcelable(SOURCE_EXTRA, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _source = getArguments().getParcelable(SOURCE_EXTRA);
        _articleId = getArguments().getString(ARTICLE_ID_EXTRA);
        getPresenter().init(_source);
    }

    @Override
    public void loadArticles(final List<Article> articles) {
        ArticleAdapter adapter = new ArticleAdapter(getFragmentManager(), articles, _source);
        viewPager.setAdapter(adapter);
        for (int i = 0; i < articles.size(); i++){
            if (articles.get(i).getId().equals(_articleId)){
                viewPager.setCurrentItem(i);
                return;
            }
        }
    }

    @Override
    public String getTitle() {
        return null;
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
    public ArticleDetailsPresenter getPresenter() {
        return presenter;
    }
}
