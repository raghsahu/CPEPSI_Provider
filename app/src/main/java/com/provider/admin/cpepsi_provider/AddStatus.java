package com.provider.admin.cpepsi_provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Model.RequestModel;
import com.provider.admin.cpepsi_provider.Util.Connectivity;

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

public class AddStatus extends AppCompatActivity {
    EditText apvName, apvEmail, apvMobile, apvDescription;
    Button accept, decline;
    String Name, Email, Mobile, Description, Id,CusId,ProvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_status);

        apvName = (EditText) findViewById(R.id.apvName);
        apvEmail = (EditText) findViewById(R.id.apvEmail);
        apvMobile = (EditText) findViewById(R.id.apvMobile);
        apvDescription = (EditText) findViewById(R.id.apvDescription);
        accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);

        if (getIntent() != null) {
            RequestModel requestModel1 = (RequestModel) getIntent().getSerializableExtra("RequestModel");
            Name = requestModel1.getName();
            Email = requestModel1.getEmail();
            Mobile = requestModel1.getContact();
            Description = requestModel1.getDiscription();
            CusId = requestModel1.getCustomerId();
            ProvId = requestModel1.getProviderId();
            Id = requestModel1.getPrId();
            apvName.setText(Name);
            apvEmail.setText(Email);
            apvMobile.setText(Mobile);
            apvDescription.setText(Description);

            if (requestModel1.getProstatus().equals("3")){
                decline.setEnabled(false);
                accept.setEnabled(false);
                Toast.makeText(this, "This request is already completed", Toast.LENGTH_LONG).show();
            }

        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Connectivity.isNetworkAvailable(AddStatus.this)) {
                    new PostAccept().execute();
                } else {
                    Toast.makeText(AddStatus.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Connectivity.isNetworkAvailable(AddStatus.this)) {
                    new PostDecline().execute();
                } else {
                    Toast.makeText(AddStatus.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //-------------------------------------------------------------
    public class PostAccept extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddStatus.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/approve_decline");
//                URL url = new URL("https://www.paramgoa.com/cpepsi/api/approve_decline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("pr_id", Id);
                postDataParams.put("status", "1");
                postDataParams.put("customer_id", CusId);
                postDataParams.put("provider_id", ProvId);

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

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                String s = result.toString();
                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    String responce = jsonObject.getString("responce");

                    if (responce.equals("true")) {
                        apvName.setText("");
                        apvEmail.setText("");
                        apvMobile.setText("");
                        apvDescription.setText("");
                        Intent intent = new Intent(AddStatus.this,Home_navigation.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddStatus.this, "Some Problem", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

    //------------------------------------------------------------

    public class PostDecline extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddStatus.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/approve_decline");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/approve_decline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("pr_id", Id);
                postDataParams.put("status", "2");
                postDataParams.put("customer_id", CusId);
                postDataParams.put("provider_id", ProvId);

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

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                String s = result.toString();
                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    String responce = jsonObject.getString("responce");

                    if (responce.equals("true")) {
                        apvName.setText("");
                        apvEmail.setText("");
                        apvMobile.setText("");
                        apvDescription.setText("");
                        Intent intent = new Intent(AddStatus.this,Home_navigation.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddStatus.this, "Some Problem", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }
    //------------------------------------------------------------
}
