package com.kita.skdnews.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.kita.skdnews.helper.Extra;

import java.util.Date;

@Entity(tableName = "tb_news")
public class DSNews {
    @PrimaryKey(autoGenerate = true)
    public int news_id;

    @ColumnInfo(name = "src_id")
    public String src_id;

    @ColumnInfo(name = "src_name")
    public String src_name;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "desc")
    public String desc;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "url_to_img")
    public String url_to_img;

    @ColumnInfo(name = "pub_date")
    @TypeConverters({Extra.class})
    public Date pub_date;


    @Ignore
    public DSNews(int news_id, String src_id, String src_name, String author, String title, String desc, String url, String url_to_img) {
        this.news_id = news_id;
        this.src_id = src_id;
        this.src_name = src_name;
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.url_to_img = url_to_img;
        this.pub_date = pub_date;
    }

    public DSNews(String src_id, String src_name, String author, String title, String desc, String url, String url_to_img) {
        this.news_id = news_id;
        this.src_id = src_id;
        this.src_name = src_name;
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.url_to_img = url_to_img;
        this.pub_date = pub_date;
    }

    public Date getDate() {
        return pub_date;
    }

    public void setDate(Date date) {
        this.pub_date = date;
    }
}
