package com.example.proiectsemestru.Entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectsemestru.Entities.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    void insert(Task task);

    @Insert
    void insert(List<Task> listaTaskuri);

    @Query("select * from taskuri")
    List<Task> getAll();

    @Query("select * from taskuri where id=:idtask")
    Task getTaskByID(int idtask);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("select * from taskuri where zi=:data and idUser=:idUser")
    List<Task> getTasksByDate(int idUser, long data);



}
