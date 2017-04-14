package ua.in.zeusapps.ukrainenews.modules.root;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.FragmentHelper;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.common.MvpActivity;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesFragment;

@Layout(R.layout.activity_root)
public class RootActivity
        extends MvpActivity
        implements RootView {

    private final static int PERIOD_TO_CLOSE = 2000;
    private long _lastPressedTimestamp;
    @InjectPresenter
    RootPresenter presenter;

    @BindView(R.id.activity_root_content)
    FrameLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentHelper.add(
                getSupportFragmentManager(),
                new SourcesFragment(),
                R.id.activity_root_content);
    }

    @Override
    public void showMessage(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(rootView, message, Snackbar.LENGTH_LONG);
        int color = ContextCompat
                .getColor(this, android.R.color.holo_green_dark);
        snackbar.getView()
                .setBackgroundColor(color);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        int count = FragmentHelper.getStackCount(getSupportFragmentManager());

        if (count > 1){
            super.onBackPressed();
        } else {
            tryClose();
        }
    }

    private void tryClose(){
        long timestamp = System.currentTimeMillis();
        if (timestamp - _lastPressedTimestamp < PERIOD_TO_CLOSE){
            finish();
            System.exit(0);
            return;
        }

        _lastPressedTimestamp = timestamp;
        showMessage(getString(R.string.root_activity_close_notification));
    }
}
