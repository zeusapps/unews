package ua.in.zeusapps.ukrainenews.components.main.fragments.articles;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.BaseViewHolder;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleAdapter extends RecyclerViewAdapter<Article>{
    private final Formatter _formatter;
    private final Source _source;
    private final int _itemTemplateId;
    public ArticleAdapter(
            Context context,
            Formatter formatter,
            Source source,
            @LayoutRes int itemTemplateId) {
        super(context);
        _formatter = formatter;
        _itemTemplateId = itemTemplateId;
        _source = source;
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
            sourceTextView.setText(_source.getTitle());

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
}
