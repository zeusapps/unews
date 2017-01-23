package ua.in.zeusapps.ukrainenews.modules.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        ((App)getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new SourceAdapter();
        recyclerView.setAdapter(_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.update(sources);
        _adapter.notifyDataSetChanged();
    }

    public class SourceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.activity_main_item_template_sourceTitle)
        TextView sourceTitle;

        public SourceHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void update(Source source){
            sourceTitle.setText(source.getTitle());
        }
    }

    public class SourceAdapter extends RecyclerView.Adapter<SourceHolder>{

        private ArrayList<Source> _sources;

        private SourceAdapter() {
            this._sources = new ArrayList<>();
        }

        private void update(List<Source> sources){
//            int count = _sources.size();
//            _sources.clear();
//            notifyItemRangeRemoved(0, count);
            _sources.addAll(sources);
            notifyDataSetChanged();
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.activity_main_item_template, parent, false);
            return new SourceHolder(view);
        }

        @Override
        public void onBindViewHolder(SourceHolder holder, int position) {
            holder.update(_sources.get(position));
        }

        @Override
        public int getItemCount() {
            return _sources.size();
        }
    }
}
