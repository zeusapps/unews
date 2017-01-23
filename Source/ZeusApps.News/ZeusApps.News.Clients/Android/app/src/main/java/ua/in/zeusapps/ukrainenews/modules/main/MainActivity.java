package ua.in.zeusapps.ukrainenews.modules.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceService;
import ua.in.zeusapps.ukrainenews.services.SourceService;

public class MainActivity extends AppCompatActivity implements MainActivityMVP.View {

    private SourceAdapter _adapter;

    @BindView(R.id.activity_main_sourcesRecyclerView)
    RecyclerView recyclerView;

    @Inject
    MainActivityMVP.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //recyclerView = (RecyclerView) findViewById(R.id.activity_main_sourcesRecyclerView);
        ((App)getApplication()).getComponent().inject(this);
                ButterKnife.bind(this);

        _adapter = new SourceAdapter();
        recyclerView.setAdapter(_adapter);

        ISourceService sourceService = new SourceService();

        sourceService.getSources()
                .subscribe(new Subscriber<List<Source>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Source> sources) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.update(sources);
    }

    private class SourceHolder extends RecyclerView.ViewHolder {
        public SourceHolder(View itemView) {
            super(itemView);
        }
    }

    private class SourceAdapter extends RecyclerView.Adapter<SourceHolder>{

        private ArrayList<Source> _sources;

        private SourceAdapter() {
            this._sources = new ArrayList<>();
        }

        private void update(List<Source> sources){
            int count = _sources.size();
            _sources.clear();
            notifyItemRangeRemoved(0, count);
            _sources.addAll(sources);
            notifyItemRangeInserted(0, _sources.size());
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(SourceHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
