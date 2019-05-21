package com.provider.admin.cpepsi_provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.androidquery.util.AQUtility.getContext;

public class OtpActivity extends AppCompatActivity {
    EditText ed_otp;
    String Ed_otp;
    Button bt_verify;

    String TypeofFirm, Service, Sub_service, Number, Designation, City, State, Place, Name, Emailid, Password, Business,
            Dob, Adharno;
    SessionManager manager;
    String id;
    String name="";
    String emailid="";
    String Service1="";
    String ServiceSubCategory="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        manager = new SessionManager(this);

        ed_otp = (EditText) findViewById(R.id.ed_otp);
        bt_verify = (Button) findViewById(R.id.bt_verify);

        Ed_otp = getIntent().getStringExtra("Otp");
        ed_otp.setText(Ed_otp);

        TypeofFirm = getIntent().getStringExtra("TypeofFirm");
        Service = getIntent().getStringExtra("service");
        Sub_service = getIntent().getStringExtra("sub_servicee");
        Number = getIntent().getStringExtra("number");
        Designation = getIntent().getStringExtra("Designation");
        City = getIntent().getStringExtra("City");
        State = getIntent().getStringExtra("state");
        Place = getIntent().getStringExtra("place");
        Name = getIntent().getStringExtra("name");
        Emailid = getIntent().getStringExtra("emailid");
        Password = getIntent().getStringExtra("password");
        Business = getIntent().getStringExtra("business");
        Dob = getIntent().getStringExtra("dob");
        Adharno = getIntent().getStringExtra("adharno");

        Log.e("Sub_service",Sub_service);
        // Sub_service="hardware";

        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ed_otp = getIntent().getStringExtra("Otp");
                ed_otp.setText(Ed_otp);

                new CompleteReg().execute();

            }
        });
    }

    private class CompleteReg extends AsyncTask<String, Void, String> {

        //  ProgressDialog dialog;

//        @Override
//        protected void onPreExecute() {
//            dialog = new ProgressDialog(getContext());
//            dialog.show();
//
//            super.onPreExecute();
//        }


        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/OtpMatch");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/OtpMatch");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("TypeofFirm", TypeofFirm);
                postDataParams.put("service", Service);
                postDataParams.put("sub_service", Sub_service);
                postDataParams.put("number", Number);
                postDataParams.put("Designation", Designation);
                postDataParams.put("City", City);
                postDataParams.put("state", State);
                postDataParams.put("place", Place);
                postDataParams.put("name", Name);
                postDataParams.put("emailid", Emailid);
                postDataParams.put("password", Password);
                postDataParams.put("business", Business);
                postDataParams.put("dob", Dob);
                postDataParams.put("adharno", Adharno);
                postDataParams.put("otp", Ed_otp);


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


        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            if (result != null) {
                //  dialog.dismiss();


                Log.e("PostRegistration", result.toString());

                try {
                    // Toast.makeText(OtpActivity.this, "reg+"+result, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(result);
                    String res = jsonObject.getString("responce");
                    if (res.equals("true")) {
                        manager.malegaonLogin();
                        JSONObject object = jsonObject.getJSONObject("data");
                        id = object.getString("user_id");
                        name = object.getString("name");
                        emailid = object.getString("emailid");
                        Service1 = object.getString("Service");
                        ServiceSubCategory = object.getString("ServiceSubCategory");

                        AppPreference.setId(OtpActivity.this, id);
                        AppPreference.setNamee(OtpActivity.this, name);
                        AppPreference.setEmail(OtpActivity.this, emailid);
                        AppPreference.setService(OtpActivity.this, Service1);
                        AppPreference.setSubservice(OtpActivity.this, ServiceSubCategory);
                        Intent to_completion = new Intent(OtpActivity.this, Home_navigation.class);
                        startActivity(to_completion);
                        finish();
                        Toast.makeText(OtpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(OtpActivity.this, "Could not register the user", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }

}
