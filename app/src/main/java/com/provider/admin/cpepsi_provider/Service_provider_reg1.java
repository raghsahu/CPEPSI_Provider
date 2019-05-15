package com.provider.admin.cpepsi_provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Adapters.SubCategoryAdapter;
import com.provider.admin.cpepsi_provider.Model.ServiceModel;
import com.provider.admin.cpepsi_provider.Model.SubCategoryModel;
import com.provider.admin.cpepsi_provider.Model.SubServiceModel;
import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.SharedPreferences.SessionManager;
import com.provider.admin.cpepsi_provider.Util.HttpHandler;

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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Service_provider_reg1 extends AppCompatActivity {

    Spinner type_spinner;
    EditText loc_e_t;
    ImageView loc_trace;
    Button sbmt;

    public int selected_ser_type;
    SessionManager manager;

    Spinner spin_service;
    ArrayList<String> ChooseService = new ArrayList<String>();
    private ArrayAdapter<String> serviceAdapter;
    private ArrayList<ServiceModel> serviceList = new ArrayList<ServiceModel>();
    public HashMap<Integer, ServiceModel> ServiceHashMap = new HashMap<Integer, ServiceModel>();

    Spinner sub_services_drop;
    ArrayList<String> ChooseSubService = new ArrayList<String>();
    private ArrayAdapter<String> SubserviceAdapter;
    private ArrayList<SubServiceModel> SubserviceList = new ArrayList<SubServiceModel>();
    public HashMap<Integer, SubServiceModel> SubServiceHashMap = new HashMap<Integer, SubServiceModel>();

    String location_from_map;
    String Address;
    String Spin_Service, Spin_SubService;
    String Service_Id;
    String Sub_Service_Id;
    RecyclerView subType;
    SubCategoryAdapter subCategoryAdapter;
    private ArrayList<SubCategoryModel> subcatlist;


    @Override
    public void onBackPressed() {
        manager.setService_selected("null");
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_reg1);

        subcatlist = new ArrayList<>();
        loc_e_t = findViewById(R.id.loc_e_t);
        loc_trace = findViewById(R.id.loc_trace);
        sbmt = findViewById(R.id.sbmt);
        subType = findViewById(R.id.subType);

        spin_service = (Spinner) findViewById(R.id.service_drop);
        sub_services_drop = (Spinner) findViewById(R.id.sub_type_drop);
        type_spinner = (Spinner) findViewById(R.id.type_drop);

        manager = new SessionManager(this);
//***********************************************************************
        loc_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_get_location = new Intent(Service_provider_reg1.this, Get_locattion_activity.class);
                go_get_location.putExtra("ser_ty2", selected_ser_type);
                manager.setService_selected(String.valueOf(selected_ser_type));
                startActivity(go_get_location);
                finish();
            }
        });
//********************************************************************
        selected_ser_type = getIntent().getIntExtra("ser_typ", 4);
        Toast.makeText(this, "servTypeId" + selected_ser_type, Toast.LENGTH_SHORT).show();

        try {
            if (manager.getServiceSelected().equals("null")) {
                new ServiceExecuteTask(String.valueOf(selected_ser_type)).execute();
            } else {
                new ServiceExecuteTask(manager.getServiceSelected().toString()).execute();
            }
        } catch (Exception e) {

        }
        //-************************************

        location_from_map = getIntent().getStringExtra("my_loc");

        try {
            if (!location_from_map.isEmpty()) {
                loc_e_t.setText(location_from_map);
                // new Get_All_Services(service_from_location).execute();
            }
        } catch (NullPointerException e) {

        }
//***************************************************************************************************
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address = loc_e_t.getText().toString();
                try {
                    Spin_Service = spin_service.getSelectedItem().toString();

                    Spin_SubService = sub_services_drop.getSelectedItem().toString();
                } catch (Exception e) {

                }

                if (loc_e_t.getText().toString().isEmpty()) {
                    loc_e_t.setError("Please fill complete address");
                } else {
                    Intent to_provider = new Intent(Service_provider_reg1.this, Profession_As_Provider.class);
                    to_provider.putExtra("type_spinner", type_spinner.getSelectedItem().toString());
                    to_provider.putExtra("loc_i", loc_e_t.getText().toString());

                    to_provider.putExtra("service_in", Service_Id);
                    to_provider.putExtra("sub_service_in", Sub_Service_Id);
                    if (spin_service.getSelectedItem().toString().equals("Select Service")) {
                        ChooseSubService.clear();
                        ChooseSubService.clear();
                        spin_service.setBackgroundColor(Color.RED);
                        if (spin_service.getSelectedItem().toString().equals("Select sub service")) {
                            sub_services_drop.setBackgroundColor(Color.RED);
                        }
                    } else {
                        startActivity(to_provider);
                        finish();
                    }


                    Toast.makeText(Service_provider_reg1.this, "ser_id" + Service_Id, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Service_provider_reg1.this, "sub_ser_id" + Sub_Service_Id, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //*****************************************************************************************
        spin_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (SubserviceList.size() != 0) {
                        ChooseSubService.clear();
                        SubserviceAdapter.notifyDataSetChanged();
                        sub_services_drop.setAdapter(null);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < ServiceHashMap.size(); i++) {
                    String s = spin_service.getItemAtPosition(position).toString();
                    if (ServiceHashMap.get(i).getService_name().equals(spin_service.getItemAtPosition(position))) {
                        Service_Id = ServiceHashMap.get(i).getSer_id();
                      //  new SubServiceExecuteTask(ServiceHashMap.get(i).getSer_id()).execute();
                       new PostSubcategory(ServiceHashMap.get(i).getSer_id()).execute();

                        Toast.makeText(Service_provider_reg1.this, "ser_id" + Service_Id, Toast.LENGTH_SHORT).show();

                    }
                  /*  else if(!ServiceHashMap.get(i).getService_name().equals(spin_service.getItemAtPosition(position)) &&spin_service.getItemAtPosition(position).toString().equals("Select Service")){
                        new SubServiceExecuteTask("200").execute();

                    }*/
//                    else {
//
//                        Service_Id= ServiceHashMap.get(i).getSer_id();
//                        new SubServiceExecuteTask(ServiceHashMap.get(i).getSer_id()).execute();
//                    }

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //*************************************************************

        try {
            //if (SubserviceList.size() != 0) {

            sub_services_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    for (int i = 0; i < SubServiceHashMap.size(); i++) {
                        Sub_Service_Id = SubServiceHashMap.get(i).getSub_ser_id();

                        if (SubServiceHashMap.get(i).getSub_service_name().equals(sub_services_drop.getItemAtPosition(position))) {
                            // Sub_Service_Id= SubServiceHashMap.get(i).getSub_ser_id();
                            Toast.makeText(Service_provider_reg1.this, "sub_ser_id" + Sub_Service_Id, Toast.LENGTH_SHORT).show();

                        }

                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //**************************************************************

    private class ServiceExecuteTask extends AsyncTask<String, Integer, String> {

        String output = "";

        String select_ser_type_id;
        URL url;

        public ServiceExecuteTask(String selected_ser_type) {
            this.select_ser_type_id = selected_ser_type;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject postDataParams = new JSONObject();
            try {
                url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");
                //     url = new URL("https://www.paramgoa.com/cpepsi/api/Get_Services");
                postDataParams.put("type", select_ser_type_id);


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
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(output);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        ChooseService.add("Select Service");
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                          /*  if(i==0)
                            {
                                serviceList.add(new ServiceModel(String.valueOf("0") , "<--Please Select Service-->"));
                            }*/
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String ser_id = jsonObject1.getString("id");
                            String Service_name = jsonObject1.getString("Service");
                            String type = jsonObject1.getString("type");
                            String image = jsonObject1.getString("image");
                            String status = jsonObject1.getString("status");

                            serviceList.add(new ServiceModel(ser_id, Service_name));
                            ServiceHashMap.put(i, new ServiceModel(ser_id, Service_name));
                            ChooseService.add(Service_name);

                        }

                        serviceAdapter = new ArrayAdapter<String>(Service_provider_reg1.this, android.R.layout.simple_spinner_dropdown_item, ChooseService);
                        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_service.setAdapter(serviceAdapter);


                    } else {
                        Toast.makeText(Service_provider_reg1.this, "no item found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //**************************************************************************************
    private class SubServiceExecuteTask extends AsyncTask<String, Integer, String> {

        String output = "";

        String Service_id;
        URL url;

        public SubServiceExecuteTask(String ser_id) {
            this.Service_id = ser_id;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject postDataParams = new JSONObject();
            try {
                url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");
                //   url = new URL("https://www.paramgoa.com/cpepsi/api/Get_SubServices");
                postDataParams.put("Servicesid", Service_id);


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
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(output);
                    String res = object.getString("responce");

                    if (res.equals("true")) {
                        ChooseSubService.add("Select sub service");

                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String Sub_ser_id = jsonObject1.getString("id");
                            String Sub_Service_id = jsonObject1.getString("Service");
                            String type = jsonObject1.getString("type");
                            String Sub_Service_name = jsonObject1.getString("ServiceSubCategory");
                            String status = jsonObject1.getString("status");

                            SubserviceList.add(new SubServiceModel(Sub_ser_id, Sub_Service_name));
                            SubServiceHashMap.put(i, new SubServiceModel(Sub_ser_id, Sub_Service_name));
                            ChooseSubService.add(Sub_Service_name);

                        }

                        SubserviceAdapter = new ArrayAdapter<String>(Service_provider_reg1.this, android.R.layout.simple_spinner_dropdown_item, ChooseSubService);
                        SubserviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sub_services_drop.setAdapter(SubserviceAdapter);

                        if (sub_services_drop.getSelectedItem().toString().equals("None found")) {
                            ChooseSubService.remove(0);
                        }

                        manager.setService_selected("null");

                    } else {
                        ChooseSubService.add("None found");
                        SubserviceAdapter = new ArrayAdapter<String>(Service_provider_reg1.this, android.R.layout.simple_spinner_dropdown_item, ChooseSubService);
                        SubserviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sub_services_drop.setAdapter(SubserviceAdapter);

                        manager.setService_selected("null");
                        Toast.makeText(Service_provider_reg1.this, "no item found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    //----------------------------------------------------------------

    public class PostSubcategory extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        String Service_id;

        public PostSubcategory(String ser_id) {
            this.Service_id = ser_id;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(Service_provider_reg1.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Servicesid", Service_id);

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
                    String responce = jsonObject.getString("responce");
                    if (responce.equals("true")) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i=0; i<dataArray.length(); i++){
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            String id = dataObj.getString("id");
                            String Service = dataObj.getString("Service");
                            String type = dataObj.getString("type");
                            String ServiceSubCategory = dataObj.getString("ServiceSubCategory");
                            String service_charge = dataObj.getString("service_charge");
                            String status = dataObj.getString("status");
                            subcatlist.add(new SubCategoryModel(id, Service, type, ServiceSubCategory, service_charge, status));
                            //   subcatlist.add(new SubCategoryModel(id, Service, type, ServiceSubCategory, service_charge, status));
                        }

                        subCategoryAdapter = new SubCategoryAdapter(Service_provider_reg1.this, subcatlist);
                        subType.setAdapter(subCategoryAdapter);

                        if (responce.equalsIgnoreCase("true")) {
                            dialog.dismiss();

                        } else {
                            //Toast.makeText(ScrollingActivity.this, output, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // txtresult.setText("Oops! No Data.");
                        Toast.makeText(Service_provider_reg1.this, "Oops! No Data.", Toast.LENGTH_LONG).show();
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

}
