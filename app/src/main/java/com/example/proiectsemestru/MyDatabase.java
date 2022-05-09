package com.example.proiectsemestru;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proiectsemestru.Entities.Task;
import com.example.proiectsemestru.Entities.TaskDAO;
import com.example.proiectsemestru.Entities.User;
import com.example.proiectsemestru.Entities.UserDAO;

@Database(entities = {User.class, Task.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "tasks.db";
    private static MyDatabase instance;
    private static User userCurrent;

    public User getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(User userCurrent) {
        this.userCurrent = userCurrent;
    }

    public static MyDatabase getInstance(Context context)
    {
        if(instance == null){
            instance = Room.databaseBuilder(context, MyDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract UserDAO getUseriDAO();
    public abstract TaskDAO getTaskDAO();
}
