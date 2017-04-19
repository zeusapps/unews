package ua.in.zeusapps.ukrainenews.modules.sources;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.BaseRootFragment;

@Layout(R.layout.fragment_sources)
public class SourcesFragment
    extends BaseRootFragment
    implements SourcesView {

    private Subscription clickSubscription;
    @BindView(R.id.fragment_sources_items)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_sources_textView)
    TextView textView;

    @InjectPresenter(type = PresenterType.GLOBAL)
    SourcesPresenter presenter;

    @Override
    public void showSources(final List<Source> sources) {
        final SourcesAdapter adapter = new SourcesAdapter(getContext());
        adapter.addAll(sources);
        clickSubscription = adapter.getItemClicked().subscribe(new Subscriber<Source>() {
            @Override
            public void onCompleted() { }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onNext(Source source) {
                presenter.showArticles(source);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showError() {
        //TODO implement show error
    }

    @Override
    public String getTitle() {
        return getString(R.string.fragment_sources_title);
    }

    @Override
    public int getFabButtonIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getFabButtonAction() {
        return null;
    }

    @Override
    public MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clickSubscription.unsubscribe();
    }
}
