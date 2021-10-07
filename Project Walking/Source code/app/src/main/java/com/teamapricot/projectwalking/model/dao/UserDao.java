package com.teamapricot.projectwalking.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.teamapricot.projectwalking.model.User;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... user);
}
