package ua.in.zeusapps.ukrainenews.components.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ua.in.zeusapps.ukrainenews.components.details.fragments.ArticleDetailsFragment;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class DetailsAdapter extends FragmentPagerAdapter {

    private final List<Article> _articles;
    private final Source _source;

    public DetailsAdapter(
            FragmentManager manager,
            List<Article> articles,
            Source source) {
        super(manager);

        _articles = articles;
        _source = source;
    }

    public int find(String id){
        for (int i = 0; i < _articles.size(); i++){
            if (_articles.get(i).getId().equals(id)){
                return i;
            }
        }

        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        Article article = _articles.get(position);
        return ArticleDetailsFragment.newInstance(article, _source);
    }

    @Override
    public int getCount() {
        return _articles.size();
    }
}
