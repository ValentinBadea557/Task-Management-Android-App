package com.example.proiectsemestru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 200;
    public static final int REQUEST_CODE_EDIT = 300;
    private Intent intent;
    private Date dataSelec;
    private List<Task> currentListOfTasks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Date currentDate= Calendar.getInstance().getTime();
        dataSelec=currentDate;

        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

        ListView lv = findViewById(R.id.listview);
        CalendarView cv = findViewById(R.id.calendarView);

        /**Schimba lista in functie de data selectata*/
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Date dataSelectata = new Date(year - 1900, month, day); /**Cumva atunci cand citeste data adauga 1900 la an*/
                dataSelec=dataSelectata;

                List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelectata.getTime());
                //Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();
                currentListOfTasks.clear();
                currentListOfTasks=new ArrayList<>(listaTaskuri);


                CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                        R.layout.elemlistview, listaTaskuri, getLayoutInflater()) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        Task task = listaTaskuri.get(position);
                        TextView tvPriority = view.findViewById(R.id.tvPriority);

                        if (task.getPrioritate().equals("High")) {
                            tvPriority.setTextColor(Color.RED);
                        } else if (task.getPrioritate().equals("Medium")) {
                            tvPriority.setTextColor(Color.parseColor("#FF8000"));
                        } else if (task.getPrioritate().equals("Low")) {
                            tvPriority.setTextColor(Color.GREEN);
                        }
                        return view;
                    }
                };
                lv.setAdapter(adapter);


            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task= currentListOfTasks.get(position);
                ArrayAdapter adapter = (ArrayAdapter) lv.getAdapter();

                AlertDialog dialog = new AlertDialog.Builder(ViewActivity.this).setTitle("Stergere").setMessage("Doriti stergere?").setNegativeButton("NU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentListOfTasks.remove(task);
                        adapter.notifyDataSetChanged();

                        db.getTaskDAO().delete(task);

                    }
                }).create();

                dialog.show();

                return true;
            }
        });

        FloatingActionButton btn = findViewById(R.id.floatingActionButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meniu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optiune1:
                Intent intentmaps = new Intent(this, MapsActivity.class);
                CalendarView cv = findViewById(R.id.calendarView);

                Log.e("Date",dataSelec.toString());
                intentmaps.putExtra("currentDate", dataSelec.toString());

                startActivity(intentmaps);
                break;

            case R.id.optiune2:
                Intent intentDesen= new Intent(this, BarChartActivity.class);
                intentDesen.putExtra("listaTaskuri",(ArrayList)currentListOfTasks);
                startActivity(intentDesen);

                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

        currentListOfTasks.clear();
        if (currentListOfTasks.size() == 0)
             currentListOfTasks = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelec.getTime());



        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                R.layout.elemlistview, currentListOfTasks, getLayoutInflater()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Task task = currentListOfTasks.get(position);
                TextView tvPriority = view.findViewById(R.id.tvPriority);

                if (task.getPrioritate().equals("High")) {
                    tvPriority.setTextColor(Color.RED);
                } else if (task.getPrioritate().equals("Medium")) {
                    tvPriority.setTextColor(Color.parseColor("#FF8000"));
                } else if (task.getPrioritate().equals("Low")) {
                    tvPriority.setTextColor(Color.GREEN);
                }
                return view;
            }
        };
        ListView lv=findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }
}