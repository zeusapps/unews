package ua.in.zeusapps.ukrainenews.components.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ua.in.zeusapps.ukrainenews.components.details.fragments.ArticleDetailsFragment;

class DetailsAdapter extends FragmentPagerAdapter {

    private final List<String> _articleIds;

    DetailsAdapter(
            FragmentManager manager,
            List<String> articleIds) {
        super(manager);

        _articleIds = articleIds;
    }

    int find(String id){
        for (int i = 0; i < _articleIds.size(); i++){
            if (_articleIds.get(i).equals(id)){
                return i;
            }
        }

        return -1;
    }

    String getArticleId(int position){
        return _articleIds.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailsFragment.newInstance(_articleIds.get(position));
    }

    @Override
    public int getCount() {
        return _articleIds.size();
    }
}
