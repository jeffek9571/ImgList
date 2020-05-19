package com.example.imglist;

import android.util.Log;

public class Post {

    private int id;
    private int width;
    private int height;
    private String download_url;
    private String author;
    private String total;

    public String getTotal() {
        return total;
    }

    public Post(String a, String url1){
        total=a;
        download_url=url1;
        Log.d("find", "1");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
