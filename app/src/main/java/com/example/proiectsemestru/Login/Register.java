package com.example.proiectsemestru.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiectsemestru.MyDatabase;
import com.example.proiectsemestru.R;
import com.example.proiectsemestru.Entities.User;

import java.util.List;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button create = findViewById(R.id.createAccButton);
        Button backToFirst = findViewById(R.id.backToFirstPageButton);

        TextView user = findViewById(R.id.textViewUsernameCreateAcc);
        TextView pass = findViewById(R.id.textViewPassword);
        TextView confpass = findViewById(R.id.textViewConfPassword);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isempty = false;
                if (user.getText().toString().equals("")) {
                    isempty = true;
                }
                if (pass.getText().toString().equals("")) {
                    isempty = true;
                }
                if (confpass.getText().toString().equals("")) {
                    isempty = true;
                }
                if (isempty == true) {
                    Toast.makeText(getApplicationContext(), "Toate campurile sunt obligatorii", Toast.LENGTH_LONG).show();


                } else {
                    String username = user.getText().toString();
                    String password = pass.getText().toString();
                    String confPassword = confpass.getText().toString();

                    MyDatabase db = MyDatabase.getInstance(getApplicationContext());
                    List<User> usr=db.getUseriDAO().getUserBasedOnUsername(username);

                    if (usr.size()==0){

                        if (password.equals(confPassword)) {
                            User user = new User();
                            user.setUsername(username);
                            user.setPassword(password);


                            db.getUseriDAO().insert(user);

                            Toast.makeText(getApplicationContext(), "Cont creat cu succes!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Parolele introduse difera!!", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Username existent!", Toast.LENGTH_LONG).show();

                    }
                }

            }
        });

        backToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}