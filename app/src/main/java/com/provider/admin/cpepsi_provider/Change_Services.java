package com.provider.admin.cpepsi_provider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.provider.admin.cpepsi_provider.Adapters.Service_Recycler_Adapter;
import com.provider.admin.cpepsi_provider.Java_files.Services;

import org.json.JSONArray;
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

public class Change_Services extends AppCompatActivity {
    private LinearLayoutManager layoutManager_in;
    //    ArrayAdapter<String> Service_adapter;
    int type_of_service_json, id_of_parent_Service, id_actual__of_parent_Service;
    TextView service_txt_id;
    int Service_id = 0;
    ArrayList<String> Service_names = new ArrayList<String>();
    List<Services> Services_provi_list = new ArrayList<>();
    // List<Services> serviceList_other = new ArrayList<>();
    private RecyclerView ser_list_view_change;
    String Address_from;
    int size_of_services = 0;
    private Service_Recycler_Adapter service_recycler_adapter;
    int type_of_edit = 0;
    List<String> Services_provided = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__services);

        Service_id = getIntent().getIntExtra("service_id", 3);
        new Get_All_Services_By_Id(Service_id).execute();
        service_recycler_adapter = new Service_Recycler_Adapter(this, Services_provi_list);
    }


    private class Get_All_Services_By_Id extends AsyncTask<Void, Void, String> {
        int service_id_txt;
        //String service_id_txt;
        Dialog dialog2;
        ProgressDialog dialog;
        URL url;

        View v;

        public Get_All_Services_By_Id(int service_txt_id) {
            this.service_id_txt = service_txt_id;
            this.v = v;

        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Change_Services.this);
            dialog.show();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {
            JSONObject postDataParams = new JSONObject();


            try {

                url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");
             //   url = new URL("https://www.paramgoa.com/cpepsi/api/Get_Services");
                postDataParams.put("type", service_id_txt);


//                postDataParams.put("password", password);

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
            dialog.dismiss();
            if (result != null) {
                //  dialog.dismiss();


                Log.e("PostRegistration", result.toString());

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    boolean response = jsonObject.getBoolean("responce");
                    if (response) {
                        Services_provided.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Service_json_object = jsonArray.getJSONObject(i);
                            String serv_string_name = Service_json_object.getString("Service");
                            type_of_service_json = Service_json_object.getInt("type");
                            id_of_parent_Service = Service_json_object.getInt("id");
                            Services services = new Services(serv_string_name, id_of_parent_Service);
                            Services_provi_list.add(services);

                            ser_list_view_change = (RecyclerView) findViewById(R.id.ser_list_view_change);
                            layoutManager_in = new LinearLayoutManager(Change_Services.this);

                            service_txt_id = (TextView) findViewById(R.id.service_txt_id);


                            size_of_services++;

                        }
                        if (size_of_services == jsonArray.length()) {

                            ser_list_view_change.setLayoutManager(layoutManager_in);
                            ser_list_view_change.setAdapter(service_recycler_adapter);
//                            if (type_of_edit == 1) {
//
//
//
//
//                         }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }

}
