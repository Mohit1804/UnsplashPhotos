package com.example.internshalaproject1.model;

import retrofit2.http.Url;

public class ImageModel {

    private UrlModel urls;

    public ImageModel(UrlModel urls) {
        this.urls = urls;
    }
    public UrlModel getUrls() {
        return urls;
    }

    public void setUrls(UrlModel urls) {
        this.urls = urls;
    }



}
