package com.example.proiectsemestru;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "useri")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;

    @Ignore
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
