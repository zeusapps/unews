package ua.in.zeusapps.ukrainenews.components.main.fragments.sources;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;

@Layout(R.layout.fragment_sources)
public class SourcesFragment
    extends BaseMainFragment
    implements SourcesView {

    private Disposable _disposable;
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
        _disposable = adapter.getItemClicked().subscribe(presenter::showArticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showError() {
        NotificationHelper.showSnackbarErrorMessage(
                textView,
                getString(R.string.fragment_sources_load_failed));
    }

    @Override
    public String getTitle() {
        return App.getInstance().getString(R.string.fragment_sources_title);
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

        if (_disposable != null && !_disposable.isDisposed()){
            _disposable.dispose();
        }
    }
}
