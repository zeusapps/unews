package ua.in.zeusapps.ukrainenews.modules.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.BaseFragment;
import ua.in.zeusapps.ukrainenews.components.ApplicationComponent;

public class SettingsFragment
        extends BaseFragment
        implements SettingsMVP.View{

    public static final String TAG = SettingsFragment.class.getSimpleName();

    @BindView(R.id.include_toolbar)
    Toolbar toolbar;
    @Inject
    SettingsMVP.Presenter presenter;

    @OnClick(R.id.activity_settings_clearButton)
    public void onClearStorage(){
        getPresenter().clearStorage();
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.fragment_settings;
    }

    @Override
    public String GetTag() {
        return TAG;
    }

    @Override
    protected void onCreateViewOverride(View view, @Nullable Bundle savedInstanceState) {
        setSupportActionBar();
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getCompatActivity().onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void inject(ApplicationComponent component) {
        component.inject(this);
    }

    @NonNull
    @Override
    protected SettingsMVP.Presenter getPresenter() {
        return presenter;
    }

    private void setSupportActionBar(){
        toolbar.setTitle(getString(R.string.settings));
        getCompatActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = getCompatActivity().getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
