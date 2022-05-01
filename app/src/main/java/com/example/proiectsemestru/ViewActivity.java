package com.example.proiectsemestru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 200;
    public static final int REQUEST_CODE_EDIT = 300;
    private Intent intent;
    private Date dataSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

        ListView lv = findViewById(R.id.listview);
        CalendarView cv = findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Date dataSelectata = new Date(year - 1900, month, day); /**Cumva atunci cand citeste data adauga 1900 la an*/
                dataSelec=dataSelectata;

                List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelectata.getTime());
                Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();

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
                            tvPriority.setTextColor(Color.YELLOW);
                        } else if (task.getPrioritate().equals("Low")) {
                            tvPriority.setTextColor(Color.GREEN);
                        }


                        return view;

                    }
                };
                lv.setAdapter(adapter);


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
        }
        return false;
    }


}