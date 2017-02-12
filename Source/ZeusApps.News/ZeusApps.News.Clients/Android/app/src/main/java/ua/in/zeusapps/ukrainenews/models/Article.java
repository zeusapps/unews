package ua.in.zeusapps.ukrainenews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Article {

    public static final String PUBLISHED_FIELD_NAME = "published";
    public static final String SOURCE_ID_FIELD_NAME = "sourceId";
    public static final String ID_FIELD_NAME = "id";

    @DatabaseField(useGetSet = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(useGetSet = true)
    @SerializedName("sourceId")
    @Expose
    private String sourceId;

    @DatabaseField(useGetSet = true)
    @SerializedName("title")
    @Expose
    private String title;

    @DatabaseField(useGetSet = true)
    @SerializedName("html")
    @Expose
    private String html;

    @DatabaseField(useGetSet = true)
    @SerializedName("url")
    @Expose
    private String url;

    @DatabaseField(useGetSet = true)
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @DatabaseField(useGetSet = true)
    @SerializedName("published")
    @Expose
    private String published;

    @DatabaseField(useGetSet = true)
    @SerializedName("upvote")
    @Expose
    private Integer upvote;

    @DatabaseField(useGetSet = true)
    @SerializedName("downvote")
    @Expose
    private Integer downvote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public Integer getDownvote() {
        return downvote;
    }

    public void setDownvote(Integer downvote) {
        this.downvote = downvote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return id.equals(article.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}