package com.example.proiectsemestru.Entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectsemestru.Entities.Task;
import com.example.proiectsemestru.Entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    long insert(Task user);

    @Insert
    long insert(User user);

    @Insert
    void insert(List<User> listaUseri);

    @Query("select * from useri where username=:usern")
    List<User> getUserBasedOnUsername(String usern);

    @Query("delete from useri")
    void deleteAll();

    @Delete
    void delete(User user);

    @Update
    void update(User user);

}
