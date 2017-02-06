package ua.in.zeusapps.ukrainenews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class Source {

    public static final String KEY_FIELD = "key";

    @DatabaseField(useGetSet = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(useGetSet = true)
    @SerializedName("title")
    @Expose
    private String title;

    @DatabaseField(useGetSet = true)
    @SerializedName("encoding")
    @Expose
    private String encoding;

    @DatabaseField(useGetSet = true)
    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;

    @DatabaseField(useGetSet = true)
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @DatabaseField(useGetSet = true)
    @SerializedName("key")
    @Expose
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        return id != null ? id.equals(source.id) : source.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}