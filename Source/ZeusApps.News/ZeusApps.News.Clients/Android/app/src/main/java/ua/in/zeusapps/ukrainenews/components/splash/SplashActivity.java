package ua.in.zeusapps.ukrainenews.components.splash;

import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.common.MvpPresenter;
import ua.in.zeusapps.ukrainenews.helpers.NotificationHelper;
import ua.in.zeusapps.ukrainenews.models.Source;

@Layout(R.layout.activity_splash)
public class SplashActivity
        extends MvpActivity
        implements SplashView {

    @InjectPresenter
    SplashPresenter presenter;

    @BindView(R.id.activity_splash_status)
    TextView statusTextView;

    @Override
    protected MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {
        statusTextView.setText(getString(R.string.splash_activity_loadingStatus));
    }

    @Override
    public void showLoadingSource(Source source) {
        String text = getString(R.string.activity_splash_source_update);
        statusTextView.setText(String.format(text, source.getTitle()));
    }

    @Override
    public void showChecking() {
        statusTextView.setText(R.string.splash_activity_checkingStatus);
    }

    @Override
    public void showError() {
        statusTextView.setText(R.string.splash_activity_oopsMessage);
        NotificationHelper.showSnackbarErrorMessage(
                statusTextView,
                getString(R.string.splash_activity_errorMessage));
    }
}