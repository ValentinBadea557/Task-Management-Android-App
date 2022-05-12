package com.example.proiectsemestru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.proiectsemestru.BarChartAndMaps.BarChartActivity;
import com.example.proiectsemestru.BarChartAndMaps.MapsActivity;
import com.example.proiectsemestru.Entities.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 200;
    public static final int REQUEST_CODE_EDIT = 300;
    public static final String EDIT_TASK = "editTask";
    private Intent intent;
    private Date dataSelec;
    private List<Task> currentListOfTasks = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int idTaskSelected;
    public int poz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Date currentDate = Calendar.getInstance().getTime();
        dataSelec = currentDate;

        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

        ListView lv = findViewById(R.id.listview);
        CalendarView cv = findViewById(R.id.calendarView);

        /**Schimba lista in functie de data selectata*/
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Date dataSelectata = new Date(year - 1900, month, day); /**Cumva atunci cand citeste data adauga 1900 la an*/
                dataSelec = dataSelectata;

                List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelectata.getTime());
                //Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();
                currentListOfTasks.clear();
                currentListOfTasks = new ArrayList<>(listaTaskuri);

                createSharedPreferencesFile(listaTaskuri);
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                poz = position;
                idTaskSelected = currentListOfTasks.get(position).getId();
                Log.e("ID TASK", String.valueOf(idTaskSelected));
                intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                intent.putExtra(EDIT_TASK, currentListOfTasks.get(position));
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = currentListOfTasks.get(position);
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

                        List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelec.getTime());
                        //Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();
                        currentListOfTasks.clear();
                        currentListOfTasks = new ArrayList<>(listaTaskuri);

                        createSharedPreferencesFile(listaTaskuri);
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

    public void printJson(List<Task> lista) throws JSONException {

        JSONArray ja = new JSONArray();
        for (Task task : lista) {
            JSONObject json = new JSONObject();
            json.put("idUser", task.getIdUser());
            json.put("name", task.getName());
            json.put("zi", task.getZi());
            json.put("prioritate", task.getPrioritate());
            json.put("latitudine", task.getLatitute());
            json.put("longitudine", task.getLongitude());
            ja.put(json);
        }


        JSONObject mainObj = new JSONObject();
        mainObj.put("taskuri", ja);
        Log.e("taskuri", mainObj.toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ListView lv = findViewById(R.id.listview);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Task task = (Task) data.getSerializableExtra(CreateTaskActivity.createTask);
            if (task != null) {

                MyDatabase db = MyDatabase.getInstance(getApplicationContext());
                Log.e("task aici","123");
                db.getTaskDAO().insert(task);

                List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelec.getTime());
                //Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();
                currentListOfTasks.clear();
                currentListOfTasks = new ArrayList<>(listaTaskuri);

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
        } else if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            MyDatabase db = MyDatabase.getInstance(getApplicationContext());

            Task task = (Task) data.getSerializableExtra(CreateTaskActivity.createTask);


            Task task2 = db.getTaskDAO().getTaskByID(idTaskSelected);
            task2.setName(task.getName());
            task2.setZi(task.getZi());
            task2.setPrioritate(task.getPrioritate());
            task2.setLatitute(task.getLatitute());
            task2.setLongitude(task.getLongitude());

            Log.e("NEW TASK", task2.toString());
            db.getTaskDAO().update(task2);

            List<Task> listaTaskuri = db.getTaskDAO().getTasksByDate(db.getUserCurrent().getId(), dataSelec.getTime());
            //Toast.makeText(getApplicationContext(), String.valueOf(listaTaskuri.size()), Toast.LENGTH_LONG).show();
            currentListOfTasks.clear();
            currentListOfTasks = new ArrayList<>(listaTaskuri);

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

                Log.e("Date", dataSelec.toString());
                intentmaps.putExtra("currentDate", dataSelec.toString());

                startActivity(intentmaps);
                break;

            case R.id.optiune2:
                Intent intentDesen = new Intent(this, BarChartActivity.class);
                intentDesen.putExtra("listaTaskuri", (ArrayList) currentListOfTasks);
                startActivity(intentDesen);
                break;

            case R.id.optiune3:
                JsonParser extJ = new JsonParser() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(ViewActivity.this);
                        progressDialog.setMessage("Va rugam asteptati...");
                        progressDialog.show();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        progressDialog.cancel();

                        MyDatabase db = MyDatabase.getInstance(getApplicationContext());
                        List<Task> listaLocal = new ArrayList<>();
                        listaLocal.addAll(this.taskList);

                        for (Task task : listaLocal) {
                            task.setIdUser(db.getUserCurrent().getId());
                        }

                        db.getTaskDAO().insert(listaLocal);


                    }
                };
                try {
                    extJ.execute(new URL("https://pastebin.com/raw/sCAdesRb"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.optiune4:
                finish();
                break;
        }
        return false;
    }

    public void createSharedPreferencesFile(List<Task> lista) {
        SharedPreferences spf = getSharedPreferences("ListaTaskuri", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("Numar taskuri", lista.size());
        for (int i = 0; i < lista.size(); i++) {
            editor.putString("Denumire " + i, lista.get(i).getName());
            editor.putString("Ziua executiei " + i, lista.get(i).getZi().toString());
            editor.putString("Prioritate " + i, lista.get(i).getPrioritate());
            editor.putString("Latitudine " + i, lista.get(i).getLatitute());
            editor.putString("Longitudine " + i, lista.get(i).getLongitude());

        }
        editor.apply();
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
        ListView lv = findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }
}