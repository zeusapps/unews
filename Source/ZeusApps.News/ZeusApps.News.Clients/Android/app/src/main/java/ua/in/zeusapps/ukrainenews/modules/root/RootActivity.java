package ua.in.zeusapps.ukrainenews.modules.root;

import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.FragmentHelper;
import ua.in.zeusapps.ukrainenews.modules.sources.SourcesFragment;

public class RootActivity
        extends MvpAppCompatActivity
        implements RootView {

    private final static int PERIOD_TO_CLOSE = 2000;
    private long _lastPressedTimestamp;
    @InjectPresenter
    RootPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        FragmentHelper.add(
                getSupportFragmentManager(),
                new SourcesFragment(),
                R.id.activity_root_content);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
