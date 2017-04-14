package ua.in.zeusapps.ukrainenews.modules.sources;

import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.FragmentHelper;

public class RootActivity
        extends MvpAppCompatActivity
        implements RootView {

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
}
