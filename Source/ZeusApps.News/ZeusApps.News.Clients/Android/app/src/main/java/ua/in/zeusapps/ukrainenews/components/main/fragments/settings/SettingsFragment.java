package ua.in.zeusapps.ukrainenews.components.main.fragments.settings;

import android.support.annotation.NonNull;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;

@Layout(R.layout.fragment_settings)
public class SettingsFragment
        extends BaseMainFragment
        implements SettingsView {

    @InjectPresenter
    SettingsPresenter presenter;

    @OnClick(R.id.activity_settings_clearButton)
    public void onClearStorage(){
        getPresenter().clearStorage();
    }

    @NonNull
    @Override
    public SettingsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void articlesCleared() {
        showInfo(getString(R.string.fragment_settings_articles_cleared));
    }

    @Override
    public String getTitle() {
        return getString(R.string.fragment_settings_title);
    }

    @Override
    public int getFabButtonIcon() {
        return 0;
    }

    @Override
    public View.OnClickListener getFabButtonAction() {
        return null;
    }
}
