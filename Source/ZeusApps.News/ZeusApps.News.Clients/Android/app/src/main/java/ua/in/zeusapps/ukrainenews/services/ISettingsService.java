package ua.in.zeusapps.ukrainenews.services;

public interface ISettingsService {
    int getFontSize();
    void setFontSize(int fontSize);
    int getArticleTemplateType();
    void setArticleTemplateType(int articleTemplateType);
}
