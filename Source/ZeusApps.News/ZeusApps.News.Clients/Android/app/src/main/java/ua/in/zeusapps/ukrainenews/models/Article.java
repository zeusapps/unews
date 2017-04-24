package ua.in.zeusapps.ukrainenews.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Article implements Parcelable {

    public static final String PUBLISHED_FIELD_NAME = "published";
    public static final String SOURCE_ID_FIELD_NAME = "sourceId";
    public static final String ID_FIELD_NAME = "id";

    @DatabaseField(useGetSet = true, columnName = ID_FIELD_NAME, id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(useGetSet = true, columnName = SOURCE_ID_FIELD_NAME)
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

    @DatabaseField(useGetSet = true, columnName = PUBLISHED_FIELD_NAME, dataType = DataType.DATE_STRING)
    @SerializedName("published")
    @Expose
    private Date published;

    @DatabaseField(useGetSet = true)
    @SerializedName("upvote")
    @Expose
    private Integer upvote;

    @DatabaseField(useGetSet = true)
    @SerializedName("downvote")
    @Expose
    private Integer downvote;

    public Article() { }

    protected Article(Parcel in) {
        id = in.readString();
        sourceId = in.readString();
        title = in.readString();
        html = in.readString();
        url = in.readString();
        imageUrl = in.readString();
        published = new Date(in.readLong());
        upvote = in.readInt();
        downvote = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(sourceId);
        dest.writeString(title);
        dest.writeString(html);
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeLong(published.getTime());
        dest.writeInt(upvote);
        dest.writeInt(downvote);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

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

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
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

    @Override
    public int describeContents() {
        return 0;
    }
}