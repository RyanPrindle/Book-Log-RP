package com.prindle.a.r.booklogrp.classes;

/**
 * Created by RP on 3/4/2015.
 */
public class Book {
    public static final String READ = "READ";
    public static final String UNREAD = "UNREAD";
    public static final String BOOKID = "id";
    public static final int FOUNDBOOK = 1;

    private int mId;
    private String mBookTitle;
    private String mBookAuthor;
    private String mReadOrNot;
    private String mSummary;
    private String mCoverUrl;
    private int mRating;

    public Book(){
    }

    public Book(String title,String author,String readOrNot,int rating,String summary){
        this.mBookTitle = title.trim();
        this.mBookAuthor = author.trim();
        this.mReadOrNot = readOrNot;
        this.mRating = rating;
        this.mSummary = summary.trim();
    }

    public Book(String title,String author,String readOrNot,int rating){
        this.mBookTitle = title.trim();
        this.mBookAuthor = author.trim();
        this.mReadOrNot = readOrNot;
        this.mRating = rating;
    }

    public Book(String title,String author,String readOrNot){
        this.mBookTitle = title.trim();
        this.mBookAuthor = author.trim();
        this.mReadOrNot = readOrNot;
    }

    public Book(String title,String author){
        this.mBookTitle = title.trim();
        this.mBookAuthor = author.trim();
    }

    public String getAuthor() {
        return this.mBookAuthor;
    }

    public void setAuthor(String author) {
        this.mBookAuthor = author.trim();
    }

    public int getID() {
        return this.mId;
    }

    public void setID(int id) {
        this.mId = id;
    }

    public String getTitle() {
        return this.mBookTitle;
    }

    public void setTitle(String title) {
        this.mBookTitle = title.trim();
    }

    public String getReadOrNot() {
        return this.mReadOrNot;
    }

    public void setReadOrNot(String readOrNot) {
        this.mReadOrNot = readOrNot;
    }

    public int getRating() {return this.mRating;}

    public void setRating(int rating) {this.mRating = rating;}

    public String getSummary() {return this.mSummary;}

    public void setSummary(String summary) {this.mSummary = summary.trim();}

    public String getCoverUrl() {return this.mCoverUrl;}

    public void setCoverUrl(String coverUrl) {this.mCoverUrl = coverUrl;}

}
