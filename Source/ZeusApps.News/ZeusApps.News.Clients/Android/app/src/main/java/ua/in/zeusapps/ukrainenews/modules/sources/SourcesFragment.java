package ua.in.zeusapps.ukrainenews.modules.sources;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpFragment;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.root.RootPresenter;

@Layout(R.layout.fragment_sources)
public class SourcesFragment
    extends MvpFragment
    implements SourcesView {

    @BindView(R.id.fragment_sources_items)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_sources_textView)
    TextView textView;

    @InjectPresenter(type = PresenterType.GLOBAL)
    SourcesPresenter presenter;

    @Override
    public void showSources(final List<Source> sources) {
        final SourcesAdapter adapter = new SourcesAdapter(sources);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        Observable.interval(10000, Long.MAX_VALUE, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        RootPresenter.getInstance().showArticles(sources.get(0));
                    }
                });
    }

    @Override
    public void showError() {
        //TODO implement show error
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sources_fragment_item_template_textView)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void update(Source source){
            textView.setText(source.getTitle());
        }
    }

    class SourcesAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final List<Source> _sources;

        public SourcesAdapter(List<Source> sources) {
            _sources = sources;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.fragment_sources_item_template, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Source source = _sources.get(position);

            holder.update(source);
        }

        @Override
        public int getItemCount() {
            return _sources.size();
        }
    }
}
