package com.example.abhishek.catalogwithretro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Book implements Parcelable{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("pages")
    @Expose
    private int pages;
    @SerializedName("authorId")
    @Expose
    private String authorId;
    @SerializedName("genreId")
    @Expose
    private String genreId;
    @SerializedName("_id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Book() {
    }

    public Book(String name, String language, String published, int pages, String authorId, String genreId) {
        this.name = name;
        this.language = language;
        this.published = published;
        this.pages = pages;
        this.authorId = authorId;
        this.genreId = genreId;
    }

 /*   */

    protected Book(Parcel in) {
        name = in.readString();
        language = in.readString();
        published = in.readString();
        pages = in.readInt();
        authorId = in.readString();
        genreId = in.readString();
        id = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(language);
        dest.writeString(published);
        dest.writeInt(pages);
        dest.writeString(authorId);
        dest.writeString(genreId);
        dest.writeString(id);
    }
}
