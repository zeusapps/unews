package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleAdapter extends BaseAdsAdapter<Article>{

    private Formatter _formatter;

    public ArticleAdapter(Activity activity, Formatter formatter) {
        super(activity);

        _formatter = formatter;
    }

    @Override
    public ArticleHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        View view = getSimpleView(R.layout.fragment_article_item_template, parent);
        return new ArticleHolder(view);
    }

    class ArticleHolder extends BaseViewHolder<Article> {
        @BindView(R.id.fragment_article_item_template_published)
        TextView publishedTextView;
        @BindView(R.id.fragment_article_item_template_title)
        TextView titleTextView;
        @BindView(R.id.fragment_article_item_template_image)
        ImageView articleImageView;

        ArticleHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(Context context, Article article) {
            super.update(context, article);

            publishedTextView.setText(_formatter.formatDate(article.getPublished()));
            titleTextView.setText(article.getTitle());

            String url = article.getImageUrl();

            if (url == null){
                articleImageView.setVisibility(View.GONE);
            }
            else {
                articleImageView.setVisibility(View.VISIBLE);
                Picasso
                        .with(context)
                        .load(url)
                        .into(articleImageView);
            }
        }
    }
}
