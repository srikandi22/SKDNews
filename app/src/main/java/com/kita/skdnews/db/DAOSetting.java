package com.kita.skdnews.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DAOSetting {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSetting(DSSettings setting);

    @Update
    int updateSetting(DSSettings news);

    @Query("SELECT * FROM tb_setting WHERE setting_id = :id LIMIT 1")
    DSSettings getSettingByID(int id);
}
