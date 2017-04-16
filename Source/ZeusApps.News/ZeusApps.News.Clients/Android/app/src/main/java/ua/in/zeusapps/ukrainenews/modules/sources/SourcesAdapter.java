package ua.in.zeusapps.ukrainenews.modules.sources;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.BaseViewHolder;
import ua.in.zeusapps.ukrainenews.adapter.RecyclerViewAdapter;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourcesAdapter extends RecyclerViewAdapter<Source> {

    public SourcesAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater()
                .inflate(R.layout.fragment_sources_item_template, parent, false);

        return new SourceViewHolder(view);
    }

    class SourceViewHolder extends BaseViewHolder<Source>{
        @BindView(R.id.sources_fragment_item_template_textView)
        TextView textView;

        public SourceViewHolder(View view) {
            super(view);
        }

        @Override
        public void update(Context context, Source source) {
            textView.setText(source.getTitle());
        }
    }
}
