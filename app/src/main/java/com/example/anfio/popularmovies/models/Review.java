package com.example.anfio.popularmovies.models;

public class Review {

    private String mId;
    private String mAuthor;
    private String mContent;

    public Review(String mId, String mAuthor, String mContent) {
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
}
