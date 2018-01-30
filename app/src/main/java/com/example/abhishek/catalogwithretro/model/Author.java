package com.example.abhishek.catalogwithretro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Author implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("_id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Author() {
    }

    /**
     *
     * @param name
     * @param language
     * @param country
     */
    public Author(String name, String language, String country) {
        super();
        this.name = name;
        this.language = language;
        this.country = country;
    }

    protected Author(Parcel in) {
        name = in.readString();
        language = in.readString();
        country = in.readString();
        id = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
        dest.writeString(country);
        dest.writeString(id);
    }
}
