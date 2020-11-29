package com.kita.skdnews.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DAONews {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNews(DSNews news);

    @Update
    int updateNews(DSNews news);

    @Delete
    int deleteNews(DSNews news);

    @Query("DELETE FROM tb_news")
    void deleteAll();

    @Query("SELECT * FROM tb_news")
    DSNews[] getAllNews();

    @Query("SELECT * FROM tb_news WHERE news_id = :id LIMIT 1")
    DSNews getNewsByID(int id);

    @Query("SELECT * FROM tb_news WHERE UPPER(title) LIKE :key")
    DSNews[] getNewsByKeyword(String key);

    @Query("SELECT * FROM tb_news WHERE url = :url LIMIT 1")
    DSNews getNewsByUrl(String url);
}
