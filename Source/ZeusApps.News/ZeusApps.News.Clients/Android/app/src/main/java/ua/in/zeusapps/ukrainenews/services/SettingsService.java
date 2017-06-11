package ua.in.zeusapps.ukrainenews.services;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class SettingsService implements ISettingsService {

    private static final String TAG = SettingsService.class.getSimpleName();
    private static final String FONT_SIZE_KEY = "font_size";
    private static final String ARTICLE_TEMPLATE_TYPE_KEY = "article_template_type";

    public static final int ARTICLE_TEMPLATE_SMALL = 1;
    public static final int ARTICLE_TEMPLATE_BIG = 2;

    private final SharedPreferences preferences;

    @Inject
    public SettingsService(Context context) {
        preferences = context.getSharedPreferences(
                SettingsService.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    public int getFontSize() {
        return preferences.getInt(FONT_SIZE_KEY, 14);
    }

    @Override
    public void setFontSize(int fontSize) {
        preferences
                .edit()
                .putInt(FONT_SIZE_KEY, fontSize)
                .apply();
    }

    @Override
    public int getArticleTemplateType() {
        return preferences.getInt(ARTICLE_TEMPLATE_TYPE_KEY, ARTICLE_TEMPLATE_SMALL);
    }

    @Override
    public void setArticleTemplateType(int articleTemplateType) {
        preferences
                .edit()
                .putInt(ARTICLE_TEMPLATE_TYPE_KEY, articleTemplateType)
                .apply();
    }
}
