package ua.in.zeusapps.ukrainenews.components.details.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.data.IArticleRepository;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article_details)
public class ArticleDetailsFragment
    extends MvpFragment
    implements ArticleDetailsView, View.OnClickListener {

    private static final String ARTICLE_ID_EXTRA = "article_id";
    private static final String SOURCE_EXTRA = "source";
    private static final String MIME_TYPE = "text/html";
    private static final String BLANK_TITLE = " ";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    @InjectPresenter
    ArticleDetailsPresenter presenter;
    @Inject
    Formatter formatter;
    @Inject
    IArticleRepository articleRepository;
    @BindView(R.id.fragment_article_view_image)
    ImageView articleImage;
    @BindView(R.id.fragment_article_view_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_view_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_view_source)
    TextView sourceTextView;
    @BindView(R.id.fragment_article_details_nestedScrollView)
    NestedScrollView scrollView;
    @BindView(R.id.fragment_article_view_articleWebView)
    WebView articleWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public ArticleDetailsPresenter getPresenter() {
        return presenter;
    }

    public static ArticleDetailsFragment newInstance(String articleId, Source source) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID_EXTRA, articleId);
        args.putParcelable(SOURCE_EXTRA, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        final Source source = getArguments().getParcelable(SOURCE_EXTRA);
        String articleId = getArguments().getString(ARTICLE_ID_EXTRA);
        articleRepository
                .getById(articleId)
                .subscribe(new Action1<Article>() {
                    @Override
                    public void call(Article article) {
                        showArticle(article, source);
                    }
                });
        setToolbar(source);
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void showArticle(Article article, Source source) {
        Picasso
                .with(getContext())
                .load(article.getImageUrl())
                .resize(320, 256)
                .centerCrop()
                .error(R.drawable.un)
                .placeholder(R.drawable.un)
                .into(articleImage);

        String html = formatter.formatHtml(article.getHtml() + "<br><br><br><br><br>");

        titleTextView.setText(article.getTitle());
        publishedTextView.setText(formatter.formatDate(article.getPublished()));
        sourceTextView.setText(source.getTitle());
        articleWebView.clearCache(true);
        articleWebView.clearHistory();
        articleWebView.setWebChromeClient(new Client());
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                scrollView.scrollTo(0,0);
            }
        });
        articleWebView.loadDataWithBaseURL(
                source.getBaseUrl(), html, MIME_TYPE, source.getEncoding(), null);
    }

    @Override
    public void onClick(View v) {
        getPresenter().close();
    }

    private void setToolbar(Source source){
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setTitle(source.getTitle());
    }

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient {
        @Override
        public Bitmap getDefaultVideoPoster() {
            Bitmap bmp = super.getDefaultVideoPoster();
            if (bmp != null){
                return bmp;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.un);
        }


    }
}


