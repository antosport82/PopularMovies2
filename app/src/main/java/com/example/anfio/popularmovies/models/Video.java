package com.example.anfio.popularmovies.models;

public class Video {

    private String mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public Video(String mId, String mKey, String mName, String mSite, String mType) {
        this.mId = mId;
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mType = mType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
