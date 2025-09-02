package com.meditrack.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.meditrack.data.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User authenticate(String email, String password);

    @Insert
    long insert(User user);
}
