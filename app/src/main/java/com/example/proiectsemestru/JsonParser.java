package com.example.proiectsemestru;

import android.os.AsyncTask;

import com.example.proiectsemestru.Entities.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonParser extends AsyncTask<URL, Void, String> {

    public List<Task> taskList = new ArrayList<>();
    JSONArray listaTaskuri = null;

    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection conn=null;
        try {
            conn= (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            InputStream ist=conn.getInputStream();

            InputStreamReader isr =new InputStreamReader(ist);
            BufferedReader br =new BufferedReader(isr);
            String linie= null;
            String rezultat = "";
            while((linie=br.readLine())!=null)
                rezultat+=linie;

            parsareJSON(rezultat);
            return rezultat;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parsareJSON(String jsonStr){
        if(jsonStr!=null){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                listaTaskuri = jsonObject.getJSONArray("taskuri");
                for(int i=0; i<listaTaskuri.length();i++){
                    JSONObject obj = listaTaskuri.getJSONObject(i);
                    String nume = obj.getString("name");
                    Date zi = new Date(obj.getString("zi"));
                    String prioritate = obj.getString("prioritate");
                    String latitudine = obj.getString("latitudine");
                    String longitudine = obj.getString("longitudine");

                    Task task=new Task();
                    task.setName(nume);
                    task.setPrioritate(prioritate);
                    task.setZi(zi);
                    task.setLatitute(latitudine);
                    task.setLongitude(longitudine);

                    taskList.add(task);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
