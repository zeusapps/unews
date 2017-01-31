package ua.in.zeusapps.ukrainenews.modules.articleView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.Formatter;

public class ArticleViewFragment
        extends BaseFragment
        implements ArticleViewMVP.IView {

    public static final String TAG = ArticleViewFragment.class.getSimpleName();

    private static final String MIME_TYPE = "text/html";

    private Article _article;
    private Source _source;

    @BindView(R.id.fragment_article_view_title)
    TextView titleTextView;
    @BindView(R.id.fragment_article_view_published)
    TextView publishedTextView;
    @BindView(R.id.fragment_article_view_source)
    TextView sourceTextView;
    @BindView(R.id.fragment_article_view_webBrowser)
    WebView webView;

    @Inject
    ArticleViewMVP.IPresenter presenter;

    @Inject
    Formatter formatter;

    public ArticleViewFragment() {
    }

    @Override
    public void showArticle(Article article, Source source) {
        _article = article;
        _source = source;
    }

    @Override
    protected void onCreateViewOverride(View view) {
        if (_article == null){
            return;
        }

        titleTextView.setText(_article.getTitle());
        publishedTextView.setText(formatter.formatDate(_article.getPublished()));
        sourceTextView.setText(_source.getTitle());

        String html = formatter.formatHtml(_article.getHtml());

        webView.setWebChromeClient(new Client());
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(_source.getBaseUrl(), html, MIME_TYPE, _source.getEncoding(), null);
    }

    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_article_view;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    // fix of bug http://stackoverflow.com/questions/32050784/chromium-webview-does-not-seems-to-work-with-android-applyoverrideconfiguration
    private class Client extends WebChromeClient{
        @Override
        public Bitmap getDefaultVideoPoster() {
            Bitmap bmp = super.getDefaultVideoPoster();
            if (bmp != null){
                return bmp;
            }
            return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_media_video_poster);
        }
    }
}
