package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder>{

    private ArrayList<Source> _sources;
    private LayoutInflater _inflater;

    public SourceAdapter(Activity activity) {
        this._sources = new ArrayList<>();
        _inflater = activity.getLayoutInflater();
    }

    public void update(List<Source> sources){
        _sources.clear();
        _sources.addAll(sources);
        notifyItemRangeInserted(0, _sources.size());
    }

    @Override
    public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.fragment_source_item_template, parent, false);
        return new SourceAdapter.SourceHolder(view);
    }

    @Override
    public void onBindViewHolder(SourceAdapter.SourceHolder holder, int position) {
        holder.update(_sources.get(position));
    }

    @Override
    public int getItemCount() {
        return _sources.size();
    }


    public class SourceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.activity_main_item_template_sourceTitle)
        TextView sourceTitle;

        public SourceHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void update(Source source){
            itemView.setTag(source);
            sourceTitle.setText(source.getTitle());
        }
    }
}
