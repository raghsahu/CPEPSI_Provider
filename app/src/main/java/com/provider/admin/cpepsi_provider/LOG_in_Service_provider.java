package com.provider.admin.cpepsi_provider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.SharedPreferences.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LOG_in_Service_provider extends AppCompatActivity {
    TextView ser_reg;
    EditText pass_provider, emai_provider;
    Button ser_log,ser_for;
    int Service_id = 0;
    Dialog choose_service;
    //Spinner service_type_spinner;
    private View choose_to_back, choose_to_next;
    //List<String> Services_names = new ArrayList<String>();
    SessionManager manager;
    String id = "";
    String name = "";
    String emailid = "";
    String Service1 = "";
    String ServiceSubCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__service_provider);

        ser_reg = findViewById(R.id.ser_reg);
        ser_log = (Button) findViewById(R.id.ser_log);
        ser_for = (Button) findViewById(R.id.ser_for);
        pass_provider = (EditText) findViewById(R.id.pass_provider);
        emai_provider = (EditText) findViewById(R.id.ema_provider);
//        Services_names.add("Please select Service");
//        Services_names.add("Technical");
//        Services_names.add("Professional");
//        Services_names.add("Free Services");

        manager = new SessionManager(this);
        if (manager.isLoggedIn()) {
            Intent intent = new Intent(LOG_in_Service_provider.this, Home_navigation.class);
            startActivity(intent);
            finish();
        }


        ser_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pass_provider.getWindowToken(), 0);
                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (pass_provider.getText().toString().isEmpty()) {
                    pass_provider.setError("Password can not be empty");
                }
                if (emai_provider.getText().toString().isEmpty()) {
                    emai_provider.setError("Email can not be empty");
                }
                if (emai_provider.getText().toString().isEmpty() && pass_provider.getText().toString().isEmpty()) {
                    pass_provider.setError("Password can not be empty");
                    emai_provider.setError("Email can not be empty");
                } else if (!emai_provider.getText().toString().isEmpty() && !pass_provider.getText().toString().isEmpty()) {

                    new Check_login(view, emai_provider.getText().toString(), pass_provider.getText().toString()).execute();

                }
            }
        });

         ser_for.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent= new Intent(LOG_in_Service_provider.this,ForgetPassword.class);
                 startActivity(intent);
                 finish();
             }
         });

        ser_reg.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(LOG_in_Service_provider.this, Service_State_Distt_Reg_Activity.class);
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
        );
    }

    private class Check_login extends AsyncTask<Void, String, String> {
        String email, password;
        View snac_v;
        ProgressDialog dialog;

        public Check_login(View view, String email, String pass) {
            this.snac_v = view;
            this.email = email;
            this.password = pass;
        }


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(LOG_in_Service_provider.this);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/login");
           //     URL url = new URL("https://www.paramgoa.com/cpepsi/api/login");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("emailid", email);
                postDataParams.put("password", password);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            Log.e("SendJsonDataToServer>>>", result.toString());
            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("responce");
                if (response.equals("true")) {
                    manager.malegaonLogin();
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    id = jsonObject1.getString("user_id");

                    name = jsonObject1.getString("name");
                    emailid = jsonObject1.getString("emailid");
                    Service1 = jsonObject1.getString("Service");
                    ServiceSubCategory = jsonObject1.getString("ServiceSubCategory");

                    AppPreference.setId(LOG_in_Service_provider.this, id);
                    AppPreference.setNamee(LOG_in_Service_provider.this, name);
                    Log.e("name", name);
                    AppPreference.setEmail(LOG_in_Service_provider.this, emailid);
                    AppPreference.setService(LOG_in_Service_provider.this, Service1);
                    AppPreference.setSubservice(LOG_in_Service_provider.this, ServiceSubCategory);
                    Intent go_to_home = new Intent(LOG_in_Service_provider.this, Home_navigation.class);
                    startActivity(go_to_home);
                    finish();
                } else {

                    Snackbar.make(snac_v, "" + response, Toast.LENGTH_LONG).show();
                    Toast.makeText(LOG_in_Service_provider.this, "Invalid user", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (choose_service.isShowing() && choose_service != null) {
                choose_service.dismiss();
            }

        } catch (Exception e) {

        }
        super.onDestroy();
    }
}