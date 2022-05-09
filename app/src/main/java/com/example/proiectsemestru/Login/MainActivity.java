package com.example.proiectsemestru.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proiectsemestru.MyDatabase;
import com.example.proiectsemestru.R;
import com.example.proiectsemestru.Entities.User;
import com.example.proiectsemestru.ViewActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.loginButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(getApplicationContext(), Register.class);
                startActivity(intentRegister);
            }
        });


        EditText etLogin=findViewById(R.id.textViewLogin);
        EditText etPass=findViewById(R.id.textViewPasswordlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabase db=MyDatabase.getInstance(getApplicationContext());

                boolean empty=false;
                if(etLogin.getText().toString().equals("")){
                    empty=true;
                    etLogin.setError("Username este gol!");
                }
                if(etPass.getText().toString().equals("")){
                    empty=true;
                    etPass.setError("Parola este goala!");
                }

                if(!empty){
                    String username=etLogin.getText().toString();
                    String pass=etPass.getText().toString();
                    User user=new User();
                    user.setUsername(username);
                    user.setPassword(pass);

                    List<User> usrlist = db.getUseriDAO().getUserBasedOnUsername(username);
                    if(usrlist.size()!=0) {
                        if (usrlist.get(0).getPassword().equals(pass)) {
                            db.setUserCurrent(usrlist.get(0));
                            Intent intentlogin = new Intent(getApplicationContext(), ViewActivity.class);
                            startActivity(intentlogin);
                        }else{
                            Toast.makeText(getApplicationContext(), "Contul introdus nu exista!" , Toast.LENGTH_LONG).show();
                        }
                    }

                }

            }
        });

        SwitchCompat sc=findViewById(R.id.switchpasswordButton);


        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux= String.valueOf(etPass.getInputType());
                if(aux.equals("129")){
                    etPass.setInputType(131073);
                }else{
                    etPass.setInputType(129);
                }
             //   Toast.makeText(getApplicationContext(),aux , Toast.LENGTH_LONG).show();
                //129 textpass
                //131073
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyDatabase db=MyDatabase.getInstance(getApplicationContext());
    }
}