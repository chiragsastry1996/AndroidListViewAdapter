package com.example.chira.listview;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class address extends AppCompatActivity {
    Button post;
    EditText add;
    private static final String REGISTER_URL = "http://192.168.50.120/android/adr/add.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        add = (EditText)findViewById(R.id.add1);

        post = (Button)findViewById(R.id.post1);

        post.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                String adr1 = String.valueOf(add.getText());

                class RegisterUser extends AsyncTask<String, Void, String> {
                    // ProgressDialog loading;
                    RegisterUserClass ruc = new RegisterUserClass();


                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                    }

                    @Override
                    protected String doInBackground(String... params) {

                        HashMap<String, String> data = new HashMap<>();
                        data.put("adr",params[0]);

                        String result = ruc.sendPostRequest(REGISTER_URL,data);
                        return  result;
                    }
                }

                RegisterUser ru = new RegisterUser();
                ru.execute(adr1);
            }
        });



    }
}
