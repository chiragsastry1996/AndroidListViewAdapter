package com.example.chira.listview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.R.attr.button;
import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity {
    Button next,add;
    public int temp;
    ArrayList<String> cars = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    RadioGroup rgp;
    RadioButton rb;
    private static final String REGISTER_URL = "http://192.168.50.120/android/adr/adr2.php";

    public String url;
    private int i = 0;
    private static String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = "http://192.168.50.120/android/adr/adr.php?id2=3";
       rgp = (RadioGroup) findViewById(R.id.rgp1);
        add = (Button)findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                //Create the intent to start another activity
                Intent intent = new Intent(view.getContext(), address.class);
                startActivity(intent);
            }
        });



    }

    public void create(){
       // AppCompatRadioButton[] rb = new AppCompatRadioButton[temp];
        int j = 0;
        for (int i = 1; i <= temp ; i++) {
            final RadioButton rbn = new RadioButton(this);
            rbn.setId(i + 1000);
            rbn.setText(cars.get(j++));
            rgp.addView(rbn);
            rbn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int radiobuttonid = rgp.getCheckedRadioButtonId();
                    String adr = String.valueOf(rbn.getText());

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
                    ru.execute(adr);

                    Toast.makeText(getBaseContext(),rbn.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String TAG = MainActivity.class.getSimpleName();

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;



    protected void onPause(){
        super.onPause();

    }
    protected void onResume(){
        super.onResume();
        new GetContacts().execute();
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = jsonObj.getJSONArray("result");

                    for( i=0;i<contacts.length();i++) {
                        temp = contacts.length();
                        JSONObject c = contacts.getJSONObject(i);
                        cars.add(c.getString("name"));
                    }

                System.out.println("1111" + cars);



                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    create();

                }
            });
        }


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
