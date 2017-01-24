package ua.in.zeusapps.ukrainenews.modules.source;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.SourceAdapter;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceFragment extends BaseFragment implements SourceMVP.IView {

    //private OnFragmentInteractionListener mListener;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(_adapter);

    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.update(sources);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
