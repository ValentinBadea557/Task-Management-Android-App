package com.example.proiectsemestru;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Task> {

    private Context context;
    private int resource;
    private List<Task> listaTaskuri;
    private LayoutInflater layoutInflater;

    public CustomAdapter(@NonNull Context context, int resource, List<Task> list,
                         LayoutInflater layoutInflater) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.listaTaskuri = list;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(resource,parent,false);
        Task task= listaTaskuri.get(position);
        if(task!=null){
            TextView tv1 = view.findViewById(R.id.tvName);
            tv1.setText("Denumire task: "+task.getName());


            TextView tv2 = view.findViewById(R.id.tvPriority);
            tv2.setText("Prioritate: "+task.getPrioritate());
            tv2.setGravity(Gravity.CENTER);
        }
        return view;

    }
}
