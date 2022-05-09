package com.example.proiectsemestru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.proiectsemestru.Entities.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    public static final String createTask = "create task";

    private boolean onlyEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Intent intent = getIntent();

        EditText name = findViewById(R.id.editTextTaskName);
        EditText taskDate = findViewById(R.id.editTextDateTask);
        EditText taskLat = findViewById(R.id.editTextTaskLat);
        EditText taskLong = findViewById(R.id.editTextTaskLong);
        RadioGroup rgroup = findViewById(R.id.radioGroup);

        Button btnCreare = findViewById(R.id.creareTaskButton);

        if (intent.hasExtra(ViewActivity.EDIT_TASK)) {
            onlyEdit = true;
            Task taskModify = (Task) intent.getSerializableExtra(ViewActivity.EDIT_TASK);
            name.setText(taskModify.getName());
            taskDate.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(taskModify.getZi()));
            if (taskModify.getPrioritate().equals("High")) {
                rgroup.check(R.id.radioButtonHigh);
            } else if (taskModify.getPrioritate().equals("Medium")) {
                rgroup.check(R.id.radioButtonMedium);
            } else if (taskModify.getPrioritate().equals("Low")) {
                rgroup.check(R.id.radioButtonLow);
            }

            taskLat.setText(taskModify.getLatitute());
            taskLong.setText(taskModify.getLongitude());
        }

        btnCreare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean empty = false;
                if (name.getText().toString().equals("")) {
                    empty = true;
                    name.setError("Introduceti denumirea!");
                }
                if (taskDate.getText().toString().equals("")) {
                    empty = true;
                    taskDate.setError("Introduceti data!");
                }
                if (taskLat.getText().toString().equals("")) {
                    empty = true;
                    taskLat.setError("Introduceti latitudinea!");
                }
                if (taskLong.getText().toString().equals("")) {
                    empty = true;
                    taskLong.setError("Introduceti longitudinea!");
                }

                if (!empty) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/yyyy", Locale.US);
                    try {
                        MyDatabase db = MyDatabase.getInstance(getApplicationContext());

                        sdf.parse(taskDate.getText().toString());
                        Date dataExecutie = new Date(taskDate.getText().toString());
                        String nume = name.getText().toString();
                        String latitudine = taskLat.getText().toString();
                        String longitudine = taskLong.getText().toString();

                        RadioButton rdBtn = findViewById(rgroup.getCheckedRadioButtonId());
                        String priority = rdBtn.getText().toString();

                        Task task = new Task();

                        task.setIdUser(db.getUserCurrent().getId());
                        task.setName(nume);
                        task.setZi(dataExecutie);
                        task.setLatitute(latitudine);
                        task.setLongitude(longitudine);
                        task.setPrioritate(priority);
                        if (!onlyEdit)
                            db.getTaskDAO().insert(task);

                        intent.putExtra(createTask, task);
                        setResult(RESULT_OK, intent);
                        finish();


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }

            }
        });


    }
}