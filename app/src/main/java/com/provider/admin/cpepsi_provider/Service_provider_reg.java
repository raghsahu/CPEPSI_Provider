package com.provider.admin.cpepsi_provider;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Adapters.ReviewsAdapter;
import com.provider.admin.cpepsi_provider.Adapters.Service_Recycler_Adapter;
import com.provider.admin.cpepsi_provider.Adapters.SubCategoryAdapter;
import com.provider.admin.cpepsi_provider.Java_files.Services;
import com.provider.admin.cpepsi_provider.Model.ReviewsModel;
import com.provider.admin.cpepsi_provider.Model.StateModel;
import com.provider.admin.cpepsi_provider.Model.SubCategoryModel;
import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.SharedPreferences.SessionManager;
import com.provider.admin.cpepsi_provider.Thread.Popup_thread;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Service_provider_reg extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner type_spinner;
    public HashMap<Integer, String> subcatigoriesMap = new HashMap<>();
    public static Spinner sub_services_drop;
    Service_Recycler_Adapter.MyViewHolder myViewHolder;
    public static TextView services_provider_spinner, service_id_dx;
    String serv_string_name;
    List<String> categories = new ArrayList<>();
    static int selected_ser_name, selected_ser_id;
    public static Dialog Service_alert;
    SessionManager sessionManager;
    List<String> Services_provided = new ArrayList<>();
    List<String> Sub_Services_provided = new ArrayList<>();
    int type_of_edit = 0;
    ImageView loc_trace;
    ListAdapter listAdapter_service_names_other;
    static ArrayAdapter listAdapter_service_names_pro, listAdapter_service_sub_service;
    Button sbmt;
    int type_of_service_json, id_of_parent_Service, id_actual__of_parent_Service;
    AlertDialog location_permission_dialog;
    EditText loc_e_t, name_of_firm, nature_of_firm, contact_no_service, contact_of_service, email_of_service;
    private String location_from_map;
    int size_of_services = 0;
    String city_of_pro, state_of_pro;
    LinearLayout line_layout;
    Services Services_provi;
    List<Services> Services_provi_list = new ArrayList<>();
    int service_from_location, sevice_id_from_location;
    RecyclerView pop_up_recy;
    Context mContext;
    Popup_thread popup_thread;
    int popup_thread2;
    private LinearLayoutManager layoutManager_in;
    private Service_Recycler_Adapter service_recycler_adapter;
    public int got_service_id, got_service_id_b_pro;
    String Set_Services;
    int Service_ser_id;
    String s;
    Calendar myCalendar;
    ProgressDialog dialog;
    DatePickerDialog.OnDateSetListener date;
    private String dateFlage;
    private String strDateDevice;
    private String strDateServer;
    EditText date_birth;
    public  String Address;
    RecyclerView subType;
    SubCategoryAdapter subCategoryAdapter;
    private ArrayList<SubCategoryModel> subcatlist;


//    Spinner spin_state;
//    ArrayList<String>ChooseState=new ArrayList<String>();
//    private ArrayAdapter<String> stateAdapter;
//    private ArrayList<StateModel> stateList=new ArrayList<StateModel>();
//
//    Spinner spin_distt;
//    ArrayList<String>ChooseDistt=new ArrayList<String>();
//    private ArrayAdapter<String> disttAdapter;
//    private ArrayList<StateModel> disttList=new ArrayList<StateModel>();
//    String strSid="";
//
//    public HashMap<Integer, StateModel> StateHashMap = new HashMap<Integer, StateModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_reg);
        // Spinner Drop down elements
        type_spinner = (Spinner) findViewById(R.id.type_drop);
        loc_e_t = (EditText) findViewById(R.id.loc_e_t);
        name_of_firm = findViewById(R.id.name_of_firm);
        nature_of_firm = findViewById(R.id.nature_of_firm);
        contact_no_service = findViewById(R.id.contact_no_service);
        contact_of_service = findViewById(R.id.contact_of_service);
        email_of_service = findViewById(R.id.email_of_service);
        services_provider_spinner = findViewById(R.id.service_name);
        service_id_dx = findViewById(R.id.service_idxp);
        sub_services_drop = findViewById(R.id.sub_type_drop);
        line_layout = findViewById(R.id.line_layout);
        mContext = this;

        subcatlist = new ArrayList<>();
//        spin_state = (Spinner)findViewById(R.id.state_type);
//        spin_distt = (Spinner)findViewById(R.id.sub_type_distt);
//
//        new stateExecuteTask().execute();
//
//        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                //strSid = stateList.get(position).getState_id();
//                try{
//                    if(disttList.size() !=0)
//                    {
//                        ChooseDistt.clear();
//                        disttAdapter.notifyDataSetChanged();
//                        spin_distt.setAdapter(null);
//
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < StateHashMap.size(); i++)
//                {
//
//                    if (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position)))
//                    {
//                        new DisttExecuteTask(StateHashMap.get(i).getState_id()).execute();
//                    }
//                   // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
//
//
//
//                }
//
//
//            }
//
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });
//

        //****************************************************************************
         selected_ser_name = getIntent().getIntExtra("ser_typ", 4);
        sessionManager = new SessionManager(this);

        Service_ser_id = getIntent().getIntExtra("selected_ser_id", 10);
        if(selected_ser_name ==4)
        {
            selected_ser_name = Service_ser_id;
        }
        Toast.makeText(mContext, "Service id is"+Service_ser_id, Toast.LENGTH_SHORT).show();
//        service_id_dx.setText(String.valueOf(Service_ser_id));
        if (sessionManager.getSERVICE_SUB_KEY() != 10 ) {
            service_id_dx.setText(String.valueOf(sessionManager.getSERVICE_SUB_KEY()));

        }
//          if(!sessionManager.getSERVICE_SUB_KEY_ADDRESS().equals("SERVICE_SUB_KEY_ADDRESS"))
//          {
//
//          }

        Toast.makeText(mContext, "selected_ser_name is " + selected_ser_name, Toast.LENGTH_SHORT).show();

        try {
            Set_Services = sessionManager.getServiceSelected();
            services_provider_spinner.setText("" + Set_Services);
//                    ShowPopupWindow();
        } catch (Exception e) {

        }

        services_provider_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_change_services = new Intent(Service_provider_reg.this, Change_Services.class);
                sessionManager = new SessionManager(Service_provider_reg.this);
                sessionManager.setSERVICE_SUB_KEY_ADDRESS(loc_e_t.getText().toString());
                sessionManager.setSERVICE_SUB_KEY(Integer.parseInt(service_id_dx.getText().toString()));
                Toast.makeText(v.getContext(), "key address is" + sessionManager.getSERVICE_SUB_KEY_ADDRESS(), Toast.LENGTH_SHORT).show();
                if (selected_ser_name != 4) {
                    sessionManager.setSERVICE_KEY(selected_ser_name);
                    //  sessionManager.setSERVICE_SUB_KEY(Integer.parseInt(service_id_dx.getText().toString()));
                    to_change_services.putExtra("Address_n", loc_e_t.getText().toString());
                    to_change_services.putExtra("service_id", selected_ser_name);
                } else {
                    //  sessionManager.setSERVICE_SUB_KEY(Integer.parseInt(service_id_dx.getText().toString()));
                    to_change_services.putExtra("service_id", sessionManager.getServiceKey());
                }
                startActivity(to_change_services);
                //            ShowPopupWindow();
            }
        }
        );

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Society")) {
//                    s = parent.getItemAtPosition(position)
                    Toast.makeText(Service_provider_reg.this, "yes ", Toast.LENGTH_SHORT).show();
                    name_of_firm.setHint("Name of the Society");
                    nature_of_firm.setHint("Nature of the Society");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkthepermisions();
        name_of_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loc_e_t.getText().toString().isEmpty()) {
                    loc_e_t.setError("This is must field");
                }
            }
        });


//        Services_provided.add("Please select Service");
        // location = getIntent().getStringExtra("location");
        location_from_map = getIntent().getStringExtra("my_loc");
        city_of_pro = getIntent().getStringExtra("city_of_pro");
        state_of_pro = getIntent().getStringExtra("place_of_pro");
        try {
            service_from_location = getIntent().getIntExtra("selected_ser_name_back", 4);
            sevice_id_from_location = getIntent().getIntExtra("ser_id_back", 10);

            loc_e_t.setText(sessionManager.getSERVICE_SUB_KEY_ADDRESS());
        } catch (NullPointerException e) {

        }
        try {
            if (!location_from_map.isEmpty()) {
                loc_e_t.setText(location_from_map);
//                if(service_from_location != 2 ){
                new Get_All_Services(service_from_location).execute();
            }
        } catch (NullPointerException e) {

        }

        categories.add("Individual");
        got_service_id = getIntent().getIntExtra("selected_ser_id", 10);
        //for professionla or personla (spanish)
        if (selected_ser_name == 1 || selected_ser_name == 2 || selected_ser_name == 3) {
            Log.e("selected i", "" + selected_ser_name);
            new Get_All_Services(selected_ser_name).execute();
//            new Get_The_Selected( sessionManager.getSERVICE_SUB_KEY()).execute();
        }
        if (sessionManager.getSERVICE_SUB_KEY() != 10) {
            int subkey = sessionManager.getSERVICE_SUB_KEY();
         //   new PostSubcategory(sessionManager.getSERVICE_SUB_KEY()).execute();
            new PostSubcategory().execute();
        }


        sbmt = (Button) findViewById(R.id.sbmt);
        loc_trace = (ImageView) findViewById(R.id.loc_trace);
        loc_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_get_location = new Intent(Service_provider_reg.this, Get_locattion_activity.class);
                //  sessionManager.setSERVICE_SUB_KEY(Service_ser_id);
                go_get_location.putExtra("ser_ty2", selected_ser_name);
                go_get_location.putExtra("sub_ser_ty2", service_id_dx.getText().toString());
                sessionManager.setSERVICE_SUB_KEY(Integer.parseInt(service_id_dx.getText().toString()));
                startActivity(go_get_location);
//                finish();
            }
        });

        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address=loc_e_t.getText().toString();

                if(loc_e_t.getText().toString().isEmpty()){
                    loc_e_t.setError("Please fill complete address");
                }else {



            /*    if (!type_spinner.getSelectedItem().toString().isEmpty() && !name_of_firm.getText().toString().isEmpty()
                        && !nature_of_firm.getText().toString().isEmpty() && !contact_no_service.getText().toString().isEmpty() && !contact_of_service.getText().toString().isEmpty() && !email_of_service.getText().toString().isEmpty()
                        && !loc_e_t.getText().toString().isEmpty()) {*/
                    /*  if (have_a_check_on_email(email_of_service.getText().toString())) {*/
                    Intent to_provider = new Intent(Service_provider_reg.this, Profession_As_Provider.class);
                    to_provider.putExtra("type_spinner", type_spinner.getSelectedItem().toString());
                    to_provider.putExtra("Address", loc_e_t.getText().toString());
                    //    to_provider.putExtra("name_of_firm", name_of_firm.getText().toString());
                    //    to_provider.putExtra("nature_of_firm", nature_of_firm.getText().toString());
                    String ser = services_provider_spinner.getText().toString();
//                        to_provider.putExtra("service_in", services_provider_spinner.getText().toString() );
                    to_provider.putExtra("service_in", service_id_dx.getText().toString());

                    try {

                        String sub_ser = sub_services_drop.getSelectedItem().toString();
                        if (subcatigoriesMap.containsValue(sub_ser)) {
                            for (Object o : subcatigoriesMap.keySet()) {
                                if (subcatigoriesMap.get(o).equals(sub_ser)) {
                                    Toast.makeText(Service_provider_reg.this, " object key " + o.toString(), Toast.LENGTH_SHORT).show();

                                    to_provider.putExtra("sub_service_in", o.toString());

                                }
                            }
                        }

                    } catch (Exception e) {

                    }
                    to_provider.putExtra("contact_no_service", contact_no_service.getText().toString());
                    to_provider.putExtra("contact_of_service", contact_of_service.getText().toString());
                    //                      to_provider.putExtra("email_of_service", email_of_service.getText().toString());
                    to_provider.putExtra("loc_i", loc_e_t.getText().toString());
                    to_provider.putExtra("email_city_of_pro_service", city_of_pro);
                    to_provider.putExtra("state_of_pro_i", state_of_pro);
                    to_provider.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(to_provider);
                    finish();

                   /* } else {
                        email_of_service.setError("Not a valid email address");
                    }*/


                    // Toast.makeText(Service_provider_reg.this, "Submitted successfully ", Toast.LENGTH_SHORT).show();
               /* } else {
                    if (name_of_firm.getText().toString().isEmpty()) {
                        name_of_firm.setError("This field can not be empty");

                    }
                    if (contact_no_service.getText().toString().isEmpty()) {
                        contact_no_service.setError("This field can not be empty");

                    }
                    if (nature_of_firm.getText().toString().isEmpty()) {
                        nature_of_firm.setError("This field can not be empty");

                    }
                    if (contact_of_service.getText().toString().isEmpty()) {
                        contact_of_service.setError("This field can not be empty");

                    }
                    if (email_of_service.getText().toString().isEmpty()) {
                        email_of_service.setError("This field can not be empty");

                    }
                    if (loc_e_t.getText().toString().isEmpty()) {
                        loc_e_t.requestFocus();
                        loc_e_t.setError("This field can not be empty");

                    }
                    if (email_of_service.getText().toString().isEmpty()) {
                        email_of_service.setError("This field can not be empty");

                    }

                    Snackbar.make(v, "Please Select all fields ", Toast.LENGTH_LONG).show();
                }*/

                }
            }
        });
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        type_spinner.setAdapter(dataAdapter);
    }

    //----------------------------------


    private Boolean have_a_check_on_email(String email) {
        String back_email[] = {"com", "in"};
        String mid_dot = ".";
        String at_the_rate = "@";
        Boolean c1, c2, m, rate;
        c1 = email.endsWith(back_email[0]);
        c2 = email.endsWith(back_email[1]);
        m = email.contains(mid_dot);
        rate = email.contains(at_the_rate);
        if ((c1 || c2) && (m && rate)) {
            Toast.makeText(this, "Ok valid", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Not ok", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    private void checkthepermisions() {

        if (ActivityCompat.checkSelfPermission(Service_provider_reg.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("CPEPSI need to use some features of your device");
            alertDialogBuilder.setPositiveButton("Ok ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(Service_provider_reg.this, "", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Service_provider_reg.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                }
            });
            location_permission_dialog = alertDialogBuilder.create();
            location_permission_dialog.show();

            //ask for permission

        } else {

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private class Get_All_Services extends AsyncTask<Void, Void, String> {
        int service_id_txt;
        int service_id;
        ProgressDialog dialog;
        URL url;

        View v;


        public Get_All_Services(int service_txt_id) {
            this.service_id_txt = service_txt_id;

            this.v = v;

        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Service_provider_reg.this);
            try {
                dialog.show();
            } catch (Exception e) {

            }


            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {
            JSONObject postDataParams = new JSONObject();


            try {
                if (service_id_txt >= 4) {
                    url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");
                  //  url = new URL("https://www.paramgoa.com/cpepsi/api/Get_SubServices");
                    postDataParams.put("Servicesid", service_id_txt);

                } else {
                    url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");
                 //   url = new URL("https://www.paramgoa.com/cpepsi/api/Get_Services");
                    postDataParams.put("type", service_id_txt);
                }


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
                            int actual_id = Service_json_object.getInt("id");
                           // sessionManager.setSERVICE_SUB_KEY( Service_json_object.getInt("id"));
                            try {
                                serv_string_name = Service_json_object.getString("ServiceSubCategory");
                                subcatigoriesMap.put(actual_id, serv_string_name);

                            } catch (JSONException e) {
                                if (size_of_services == 0) {
                                    serv_string_name = Service_json_object.getString("Service");
                                    if (service_id_dx.getText().toString().isEmpty()) {

                                        id_actual__of_parent_Service = Service_json_object.getInt("id");
//                                            String s2 = String.valueOf(id_actual__of_parent_Service);
                                        service_id_dx.setText(String.valueOf(id_actual__of_parent_Service));
                                        Log.e("id is", "" + id_of_parent_Service);
                                        Log.e("actual id is", "" + id_actual__of_parent_Service);
                                    }


                                }
                            }


                            type_of_service_json = Service_json_object.getInt("type");
                            if (size_of_services == 0) {
                                id_of_parent_Service = Service_json_object.getInt("id");
                                Log.e("id is", "" + id_of_parent_Service);
                            }

                            if (type_of_edit == 1) {
                                Services services = new Services(serv_string_name, id_of_parent_Service);
                                Services_provi_list.add(services);
                            }

                            if (service_id_txt >= 4) {

                                Sub_Services_provided.add(serv_string_name);
                            } else {
                                Services_provided.add(serv_string_name);
                            }

                            size_of_services++;

                        }
                        if (size_of_services == jsonArray.length()) {
                            if (type_of_edit == 1) {
                                pop_up_recy.setAdapter(service_recycler_adapter);
                            }
                            Log.e("size_of", "json is" + size_of_services);
                            if (service_id_txt == 1 || service_id_txt == 2 || service_id_txt == 3) {
                                listAdapter_service_names_pro = new ArrayAdapter<String>(Service_provider_reg.this, android.R.layout.simple_spinner_item, Services_provided);
                                (listAdapter_service_names_pro).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
//                                services_provider_spinner.setAdapter( listAdapter_service_names_pro);
                                services_provider_spinner.setText(listAdapter_service_names_pro.getItem(0).toString());
//                                service_id_dx.setText(id_actual__of_parent_Service);

                                size_of_services = 0;
                                Log.e("now size_of", "json is" + size_of_services);
//                            }


                                if (!services_provider_spinner.getText().toString().isEmpty() && !service_id_dx.getText().toString().isEmpty()) {
                                    size_of_services = 0;
                                    new Get_All_Services(id_of_parent_Service).execute();

                                }

                            }
                            if (service_id_txt >= 4 ) {
                                listAdapter_service_sub_service = new ArrayAdapter<String>(Service_provider_reg.this, android.R.layout.simple_spinner_item, Sub_Services_provided);
                                (listAdapter_service_sub_service).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sub_services_drop.setAdapter(listAdapter_service_sub_service);

                            }
//
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }

    @Override
    protected void onDestroy() {
        try {
            if (location_permission_dialog.isShowing() && location_permission_dialog != null) {
                location_permission_dialog.dismiss();
            }
//
        } catch (Exception e) {

        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent to_log = new Intent(Service_provider_reg.this, LOG_in_Service_provider.class);
        startActivity(to_log);
//        services_provider_spinner.setAdapter(null);
        finish();
        super.onBackPressed();
    }

    //-----------------------------------------------------------

    public class PostSubcategory extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
                    dialog = new ProgressDialog(Service_provider_reg.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Servicesid", AppPreference.getId(Service_provider_reg.this));

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

                        subCategoryAdapter = new SubCategoryAdapter(Service_provider_reg.this, subcatlist);
                        subType.setAdapter(subCategoryAdapter);

                        if (responce.equalsIgnoreCase("true")) {
                            dialog.dismiss();

                        } else {
                            //Toast.makeText(ScrollingActivity.this, output, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // txtresult.setText("Oops! No Data.");
                        Toast.makeText(Service_provider_reg.this, "Oops! No Data.", Toast.LENGTH_LONG).show();
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

    //-----------------------------------------------------------------

   /* private class Get_The_Selected extends AsyncTask<String, Void, String> {
        Context main_context;
        ProgressDialog Service_bar;
        URL url;
        int ser_id_int;
        String serv_string_name;
        private int service_id_txt;
        private ArrayAdapter<String> listAdapter_sub_service_names;
        private ArrayAdapter<String> Adapter_sub_service_name_null;
        List<String> Sub_Services_provided = new ArrayList<>();
        List<String> Sub_Services_Null_Provided = new ArrayList<>();

        public Get_The_Selected(int service_did) {

            this.ser_id_int = service_did;
        }

        @Override
        protected void onPreExecute() {
            Service_bar = new ProgressDialog(Service_provider_reg.this);
            Service_bar.show();
            Service_bar.setCancelable(false);

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            JSONObject postDataParams = new JSONObject();

            try {
                url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");
          //      url = new URL("https://www.paramgoa.com/cpepsi/api/Get_SubServices");

                postDataParams.put("Servicesid", ser_id_int);


                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 *//* milliseconds*//*);
                conn.setConnectTimeout(15000  *//*milliseconds*//*);
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
            Service_bar.dismiss();
            if (result != null) {
                //  dialog.dismiss();


                Log.e("PostRegistration", result.toString());

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    boolean response = jsonObject.getBoolean("responce");
                    if (response) {
                        subcatigoriesMap.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Service_json_object = jsonArray.getJSONObject(i);
                            int actual_id = Service_json_object.getInt("id");
                            service_id_txt = Integer.parseInt(Service_json_object.getString("type"));
                            serv_string_name = Service_json_object.getString("ServiceSubCategory");
                            subcatigoriesMap.put(actual_id, serv_string_name);
                            Sub_Services_provided.add(serv_string_name);

                            size_of_services++;
                        }
                        if (size_of_services >= jsonArray.length()) {

                            Log.e("size_of", "json is" + size_of_services);
                            if (service_id_txt == 1 || service_id_txt == 2 || service_id_txt == 3) {
                                listAdapter_sub_service_names = new ArrayAdapter<String>(Service_provider_reg.this, android.R.layout.simple_spinner_item, Sub_Services_provided);
                                (listAdapter_sub_service_names).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
//                                services_provider_spinner.setAdapter( listAdapter_service_names_pro);
                                sub_services_drop.setAdapter(listAdapter_sub_service_names);

//                            size_of_services =0;
                                Log.e("now size_of", "json is" + size_of_services);
                            }
                        }

                    } else {
                        Sub_Services_Null_Provided.add("None found");
                        Adapter_sub_service_name_null = new ArrayAdapter<String>(Service_provider_reg.this, android.R.layout.simple_spinner_item, Sub_Services_Null_Provided);
                        (Adapter_sub_service_name_null).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
//                                services_provider_spinner.setAdapter( listAdapter_service_names_pro);
                        sub_services_drop.setAdapter(Adapter_sub_service_name_null);

                        sub_services_drop.setAdapter(Adapter_sub_service_name_null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }
    }*/
}