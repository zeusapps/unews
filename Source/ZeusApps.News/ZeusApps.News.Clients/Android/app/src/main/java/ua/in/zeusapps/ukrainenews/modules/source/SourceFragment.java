package ua.in.zeusapps.ukrainenews.modules.source;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.BaseAdapter;
import ua.in.zeusapps.ukrainenews.adapter.SourceAdapter;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceFragment
        extends BaseFragment
        implements SourceMVP.IView, BaseAdapter.OnItemClickListener<Source> {

    private OnSelectedSourceChangedListener _listener;
    private SourceAdapter _adapter;

    @BindView(R.id.fragment_source_sourcesRecyclerView)
    RecyclerView recyclerView;
    @Inject
    SourceMVP.IPresenter presenter;

    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_source;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreateViewOverride(View view) {
        _adapter = new SourceAdapter(getActivity());
        _adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(_adapter);
    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.replaceAll(sources);
        if (sources.size() > 0){
            onItemClick(sources.get(0));
        }
    }

    @Override
    public void onItemClick(Source source) {
        if (_listener != null){
            _listener.onSourceChanged(source);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnSelectedSourceChangedListener) {
            _listener = (OnSelectedSourceChangedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSelectedSourceChanged");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _listener = null;
    }

    public interface OnSelectedSourceChangedListener {
        void onSourceChanged(Source source);
    }
}
