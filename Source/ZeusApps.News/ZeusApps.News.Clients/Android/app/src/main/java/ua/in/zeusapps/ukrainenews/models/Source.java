package ua.in.zeusapps.ukrainenews.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class Source implements Parcelable {

    public static final String KEY_FIELD_NAME = "key";

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

    @DatabaseField(useGetSet = true, columnName = KEY_FIELD_NAME)
    @SerializedName("key")
    @Expose
    private String key;

    @DatabaseField(useGetSet = true)
    private int timestamp;

    public Source(){ }

    protected Source(Parcel in) {
        id = in.readString();
        title = in.readString();
        encoding = in.readString();
        baseUrl = in.readString();
        imageUrl = in.readString();
        key = in.readString();
        timestamp = in.readInt();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(encoding);
        dest.writeString(baseUrl);
        dest.writeString(imageUrl);
        dest.writeString(key);
        dest.writeInt(timestamp);
    }
}