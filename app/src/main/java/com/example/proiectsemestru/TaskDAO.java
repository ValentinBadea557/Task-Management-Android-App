package com.example.proiectsemestru;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    void insert(Task task);

    @Insert
    void insert(List<Task> listaTaskuri);

    @Query("select * from taskuri")
    List<Task> getAll();

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("select * from taskuri where zi=:data and idUser=:idUser")
    List<Task> getTasksByDate(int idUser,long data);



}
