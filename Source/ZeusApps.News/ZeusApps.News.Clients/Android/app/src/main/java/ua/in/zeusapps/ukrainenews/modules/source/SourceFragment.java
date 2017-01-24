package ua.in.zeusapps.ukrainenews.modules.source;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.SourceAdapter;
import ua.in.zeusapps.ukrainenews.common.App;
import ua.in.zeusapps.ukrainenews.models.Source;

public class SourceFragment extends Fragment implements SourceMVP.IView {

    @BindView(R.id.fragment_source_sourcesRecyclerView)
    RecyclerView recyclerView;

    @Inject
    SourceMVP.IPresenter presenter;

    private SourceAdapter _adapter;
    //private OnFragmentInteractionListener mListener;

    public SourceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);
        ButterKnife.bind(this, view);

        _adapter = new SourceAdapter(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(_adapter);


        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.update(sources);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((App) getActivity().getApplication()).getComponent().inject(this);

        presenter.setView(this);




//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
