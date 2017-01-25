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

public class ArticleAdapter extends BaseAdapter<ArticleAdapter.ArticleHolder, Article> {

    public ArticleAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSimpleView(R.layout.fragment_article_item_template, parent);
        return new ArticleHolder(view);
    }

    public class ArticleHolder extends BaseViewHolder<Article> {
        @BindView(R.id.fragment_article_item_template_published)
        TextView publishedTextView;
        @BindView(R.id.fragment_article_item_template_title)
        TextView titleTextView;
        @BindView(R.id.fragment_article_item_template_image)
        ImageView articleImageView;

        public ArticleHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(Context context, Article article) {
            super.update(context, article);

            publishedTextView.setText(article.getPublished());
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
