package ua.in.zeusapps.ukrainenews.components.main.fragments.settings;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.ukrainenews.R;
import ua.in.zeusapps.ukrainenews.common.Layout;
import ua.in.zeusapps.ukrainenews.components.main.base.BaseMainFragment;
import ua.in.zeusapps.ukrainenews.services.SettingsService;

@Layout(R.layout.fragment_settings)
public class SettingsFragment
        extends BaseMainFragment
        implements SettingsView, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.fragment_article_articleTemplateGroup)
    RadioGroup articleTemplateGroup;
    @BindView(R.id.fragment_settings_articleTemplateBig)
    RadioButton articleTemplateBig;
    @BindView(R.id.fragment_settings_articleTemplateSmall)
    RadioButton articleTemplateSmall;

    @InjectPresenter
    SettingsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPresenter().init();
    }

    @Override
    public void onResume() {
        super.onResume();

        articleTemplateGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        articleTemplateGroup.setOnCheckedChangeListener(null);
    }

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
    public void setArticleTemplateType(int articleTemplateType) {
        switch (articleTemplateType){
            case SettingsService.ARTICLE_TEMPLATE_BIG:
                articleTemplateBig.setChecked(true);
                return;
            case SettingsService.ARTICLE_TEMPLATE_SMALL:
                articleTemplateSmall.setChecked(true);
                return;
            default:
                throw new IllegalStateException("articleTemplate is incorrect");
        }
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

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.fragment_settings_articleTemplateBig:
                getPresenter().setArticleItemTemplateType(SettingsService.ARTICLE_TEMPLATE_BIG);
                break;
            case R.id.fragment_settings_articleTemplateSmall:
                getPresenter().setArticleItemTemplateType(SettingsService.ARTICLE_TEMPLATE_SMALL);
                break;
            default:
                throw new IllegalStateException("Unknown radioButton Id.");
        }
    }
}
