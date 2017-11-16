package com.example.abhishek.catalogwithretro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

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

    /**
     *  @param name
     * @param language
     * @param published
     * @param pages
     */
    public Book(String name, String language, String published, int pages) {
        super();
        this.name = name;
        this.language = language;
        this.published = published;
        this.pages = pages;
    }

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

}
