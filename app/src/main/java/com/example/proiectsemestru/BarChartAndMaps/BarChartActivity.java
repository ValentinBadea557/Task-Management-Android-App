package com.example.proiectsemestru.BarChartAndMaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.proiectsemestru.R;
import com.example.proiectsemestru.Entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartActivity extends AppCompatActivity {

    ArrayList<Task> listaTaskuri;
    Map<String,Integer> source;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        Intent intent=getIntent();

        listaTaskuri= (ArrayList<Task>) intent.getSerializableExtra("listaTaskuri");

        source=getSource(listaTaskuri);
        layout=findViewById(R.id.layoutBar);
        layout.addView(new BarChartView(getApplicationContext(),source));


    }

    private Map<String,Integer> getSource(List<Task> listaTaskuri){
        if(listaTaskuri==null || listaTaskuri.isEmpty()){
            return new HashMap<>();
        }else{
            Map<String,Integer> results = new HashMap<>();
            for(Task task:listaTaskuri){
                if(results.containsKey(task.getPrioritate())){
                    results.put(task.getPrioritate(),results.get(task.getPrioritate())+1);
                }else{
                    results.put(task.getPrioritate(),1);
                }
            }
            return results;
        }
    }


}