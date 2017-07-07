package ua.in.zeusapps.ukrainenews.components.articles;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.BaseViewHolder;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleAdapter extends RecyclerViewAdapter<Article>{
    private final Formatter _formatter;
    private final SourceTitleSelector _titleSelector;
    private final int _itemTemplateId;
    public ArticleAdapter(
            Context context,
            Formatter formatter,
            SourceTitleSelector titleSelector,
            @LayoutRes int itemTemplateId) {
        super(context);
        _formatter = formatter;
        _itemTemplateId = itemTemplateId;
        _titleSelector = titleSelector;
    }

    @Override
    protected BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater()
                .inflate(_itemTemplateId, parent, false);
        return new ArticleHolder(view);
    }

    class ArticleHolder extends BaseViewHolder<Article>{

        ArticleHolder(View view) {
            super(view);
        }

        @BindView(R.id.fragment_article_item_template_source)
        TextView sourceTextView;
        @BindView(R.id.fragment_article_item_template_published)
        TextView publishedTextView;
        @BindView(R.id.fragment_article_item_template_title)
        TextView titleTextView;
        @BindView(R.id.fragment_article_item_template_image)
        ImageView articleImageView;

        @Override
        public void update(Context context, Article article) {
            super.update(context, article);

            publishedTextView.setText(_formatter.formatDate(article.getPublished()));
            titleTextView.setText(article.getTitle());
            sourceTextView.setText(_titleSelector.getTitle(article));

            String url = article.getImageUrl();

            if (url == null){
                articleImageView.setVisibility(View.GONE);
            }
            else {
                articleImageView.setVisibility(View.VISIBLE);

                int width = 320;
                int height = 256;

                Picasso
                        .with(context)
                        .load(url)
                        .resize(width, height)
                        .centerCrop()
                        .placeholder(R.drawable.un)
                        .error(R.drawable.un)
                        .into(articleImageView);
            }
        }
    }

    interface SourceTitleSelector {
        String getTitle(Article article);
    }

    public class SingleSourceTitleSelector implements SourceTitleSelector{

        private final Source _source;

        public SingleSourceTitleSelector(Source source) {
            _source = source;
        }

        @Override
        public String getTitle(Article article) {
            return _source.getTitle();
        }
    }

    public static class MultiSourceTitleSelector implements SourceTitleSelector {

        private final List<Source> _sources;

        public MultiSourceTitleSelector(List<Source> sources) {
            _sources = sources;
        }

        @Override
        public String getTitle(Article article) {
            for (Source source: _sources) {
                if (article.getSourceId().equals(source.getKey())){
                    return source.getTitle();
                }
            }

            return "";
        }
    }
}
