package com.kita.skdnews.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_setting")
public class DSSettings {
    @PrimaryKey(autoGenerate = true)
    public int setting_id;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "apikey")
    public String apikey;

    @Ignore
    public DSSettings(int setting_id, String country, String language, String category, String apikey) {
        this.setting_id = setting_id;
        this.country = country;
        this.language = language;
        this.category = category;
        this.apikey = apikey;
    }

    public DSSettings(String country, String language, String category, String apikey) {
        this.country = country;
        this.language = language;
        this.category = category;
        this.apikey = apikey;
    }
}
