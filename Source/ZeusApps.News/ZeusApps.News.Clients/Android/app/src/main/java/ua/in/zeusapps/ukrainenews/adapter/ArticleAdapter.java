package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.models.Article;

public class ArticleAdapter extends BaseAdapter<ArticleAdapter.ArticleHolder, Article> {

    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private final SimpleDateFormat _formatterUTC;

    public ArticleAdapter(Activity activity) {
        super(activity);

        _formatterUTC = new SimpleDateFormat(DATE_FORMAT);
        _formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
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

            String published;

            try {
                Date date = _formatterUTC.parse(article.getPublished());
                published = SimpleDateFormat.getDateTimeInstance().format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                published = "";
            }

            publishedTextView.setText(published);
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
