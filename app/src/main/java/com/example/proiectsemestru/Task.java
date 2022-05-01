package com.example.proiectsemestru;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "taskuri")
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idUser;
    private String name;
    private Date zi;
    private String prioritate;
    private String latitute;
    private String longitude;

    public Task(int idUser, String name, Date zi, String prioritate, String latitute, String longitude) {
        this.idUser = idUser;
        this.name = name;
        this.zi = zi;
        this.prioritate = prioritate;
        this.latitute = latitute;
        this.longitude = longitude;
    }

    @Ignore
    public Task(int id, int idUser, String name, Date zi, String prioritate, String latitute, String longitude) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.zi = zi;
        this.prioritate = prioritate;
        this.latitute = latitute;
        this.longitude = longitude;
    }

    public Task(){}

    public String getPrioritate() {
        return prioritate;
    }

    public void setPrioritate(String prioritate) {
        this.prioritate = prioritate;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getZi() {
        return zi;
    }

    public void setZi(Date zi) {
        this.zi = zi;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", name='" + name + '\'' +
                ", zi=" + zi +
                ", prioritate='" + prioritate + '\'' +
                ", latitute=" + latitute +
                ", longitude=" + longitude +
                '}';
    }
}
