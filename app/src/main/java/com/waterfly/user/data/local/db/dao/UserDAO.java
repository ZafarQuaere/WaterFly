package com.waterfly.user.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.waterfly.user.data.network.model.UserDetails;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM UserDetails")
    LiveData<List<UserDetails>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserDetails> questions);
}
