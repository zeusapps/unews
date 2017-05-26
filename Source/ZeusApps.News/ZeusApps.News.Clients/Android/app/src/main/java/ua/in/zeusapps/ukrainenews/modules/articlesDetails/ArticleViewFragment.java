package ua.in.zeusapps.ukrainenews.modules.articlesDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.helpers.FragmentHelper;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.root.base.BaseRootFragment;
import ua.in.zeusapps.ukrainenews.services.Formatter;

@Layout(R.layout.fragment_article_view)
public class ArticleViewFragment
        extends BaseRootFragment
        implements
            ArticleViewView,
            AppBarLayout.OnOffsetChangedListener {

    private static final String ARTICLE_EXTRA = "article";
    private static final String SOURCE_EXTRA = "source";
    private static final String MIME_TYPE = "text/html";
    private static final String BLANK_TITLE = " ";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    private boolean _isVisible;
    private int _scrollRange = -1;
    private Article _article;

    Toolbar toolbar;
    @BindView(R.id.fragment_article_view_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_view_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_view_source)
    TextView sourceTextView;
    @BindView(R.id.fragment_article_view_image)
    ImageView articleImage;
    @BindView(R.id.fragment_article_view_collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.fragment_article_details_appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.fragment_article_view_articleWebView)
    WebView articleWebView;

    @Inject
    Formatter formatter;
    @InjectPresenter
    ArticleViewPresenter presenter;

    @Override
    public ArticleViewPresenter getPresenter() {
        return presenter;
    }

    public ArticleViewFragment() {
        setHasOptionsMenu(true);
    }

    public static ArticleViewFragment newInstance(Article article, Source source) {
        ArticleViewFragment fragment = new ArticleViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_EXTRA, article);
        args.putParcelable(SOURCE_EXTRA, source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar = getRootActivity().getToolbar();
        appBarLayout.addOnOffsetChangedListener(this);

        Source _source = getArguments().getParcelable(SOURCE_EXTRA);
        _article = getArguments().getParcelable(ARTICLE_EXTRA);

        showArticle(_article, _source);

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
        articleWebView.loadDataWithBaseURL(source.getBaseUrl(), html, MIME_TYPE, source.getEncoding(), null);
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void onDetach() {
        articleWebView.destroy();
        super.onDetach();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (_scrollRange == -1) {
            _scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (_scrollRange + verticalOffset == 0) {
            toolbar.setTitle(_article.getTitle());
            _isVisible = true;
        } else if(_isVisible) {
            toolbar.setTitle(BLANK_TITLE);
            _isVisible = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_article_view_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.fragment_article_view_viewOnSite:
                String url = _article.getUrl();
                if (!url.startsWith(HTTP_PREFIX) && !url.startsWith(HTTPS_PREFIX)){
                    url = HTTP_PREFIX + url;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            case android.R.id.home:
                FragmentHelper.pop(getFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fragment_article_view_shareButton)
    public void onShare(){
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share
                .putExtra(Intent.EXTRA_TITLE, _article.getTitle())
                .putExtra(Intent.EXTRA_TEXT, _article.getTitle() + " " + _article.getUrl())
                .putExtra(Intent.EXTRA_SUBJECT, _article.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            share.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(_article.getHtml()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            share.putExtra(Intent.EXTRA_ORIGINATING_URI, _article.getUrl());
        }

        share.setType("text/plain");
        startActivity(Intent.createChooser(share, _article.getTitle()));
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

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient{
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
