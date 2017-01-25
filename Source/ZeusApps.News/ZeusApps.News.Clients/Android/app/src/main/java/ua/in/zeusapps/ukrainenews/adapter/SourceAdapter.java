package ua.in.zeusapps.ukrainenews.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceAdapter extends BaseAdapter<SourceAdapter.SourceHolder, Source> {

    public SourceAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getSimpleView(R.layout.fragment_source_item_template, parent);
        return new SourceAdapter.SourceHolder(view);
    }

    public class SourceHolder extends BaseViewHolder<Source> {

        @BindView(R.id.activity_main_item_template_sourceTitle)
        TextView sourceTitle;

        public SourceHolder(View view) {
            super(view);
        }

        @Override
        public void update(Context context, Source source){
            super.update(context, source);

            sourceTitle.setText(source.getTitle());
        }
    }
}
