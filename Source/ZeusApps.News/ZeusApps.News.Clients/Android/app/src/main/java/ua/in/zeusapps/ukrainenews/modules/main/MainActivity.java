package ua.in.zeusapps.ukrainenews.modules.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.adapter.SourceAdapter;
import ua.in.zeusapps.ukrainenews.common.BaseActivity;
import ua.in.zeusapps.ukrainenews.common.BaseMVP;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.modules.source.SourceFragment;

public class MainActivity extends BaseActivity implements MainActivityMVP.View {

    private SourceAdapter _adapter;

    @BindView(R.id.activity_main_sourcesRecyclerView)
    RecyclerView recyclerView;
    @Inject
    MainActivityMVP.Presenter presenter;

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @NonNull
    @Override
    protected BaseMVP.IPresenter getPresenter() {
        return presenter;
    }


    @Override
    protected void onCreateOverride(@Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new SourceAdapter(this);
        recyclerView.setAdapter(_adapter);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_sourceFragmentPlaceholder, new SourceFragment())
                .commit();
    }

    @Override
    public void updateSources(List<Source> sources) {
        _adapter.update(sources);
    }
}
