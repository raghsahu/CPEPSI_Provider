package com.provider.admin.cpepsi_provider;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Model.StateModel;
import com.provider.admin.cpepsi_provider.Util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service_State_Distt_Reg_Activity extends AppCompatActivity {

    Spinner spin_state;
    ArrayList<String> ChooseState=new ArrayList<String>();
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList=new ArrayList<StateModel>();

    Spinner spin_distt;
    ArrayList<String>ChooseDistt=new ArrayList<String>();
    private ArrayAdapter<String> disttAdapter;
    private ArrayList<StateModel> disttList=new ArrayList<StateModel>();
    String strSid="";
    public static String Spin_state, Spin_distt;

    public HashMap<Integer, StateModel> StateHashMap = new HashMap<Integer, StateModel>();

    Spinner service_type_spinner;
    List<String> Services_names = new ArrayList<String>();

Button btn_back,btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_and_servicetype_reg);

        btn_back=(Button)findViewById(R.id.choose_back);
        btn_next=(Button)findViewById(R.id.choose_next);

        spin_state = (Spinner)findViewById(R.id.state_type);
        spin_distt = (Spinner)findViewById(R.id.sub_type_distt);
        service_type_spinner=(Spinner)findViewById(R.id.service_type_spinner);

       btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Service_State_Distt_Reg_Activity.this, LOG_in_Service_provider.class);
                startActivity(intent);
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spin_state=spin_state.getSelectedItem().toString();
                Spin_distt=spin_distt.getSelectedItem().toString();


                if (service_type_spinner.getSelectedItem().toString().equals("Please select Service")) {
                    service_type_spinner.setBackgroundColor(Color.RED);
//                  services_provider_spinner.setAdapter(null);
//                  listAdapter_service_names.notify();
                } else {
//                           service_type_spinner.setBackgroundColor(Color.parseColor("#000000"));
                    Intent to_reg = new Intent(Service_State_Distt_Reg_Activity.this, Service_provider_reg1.class);

                    if (service_type_spinner.getSelectedItemPosition() == 1) {
                        to_reg.putExtra("ser_typ", 3);
                        Log.e("selected i", "" + service_type_spinner.getSelectedItemPosition());
                    }  if (service_type_spinner.getSelectedItemPosition() == 2) {
                        to_reg.putExtra("ser_typ", 1);
                        Log.e("selected i", "" + service_type_spinner.getSelectedItemPosition());
                    }
                    if (service_type_spinner.getSelectedItemPosition() == 3) {
                        to_reg.putExtra("ser_typ", 2);
                        Log.e("selected i", "" + service_type_spinner.getSelectedItemPosition());
                    }


                    startActivity(to_reg);
                     finish();
                }
            }
        });

//************************************************
        Services_names.add("Please select Service");
        Services_names.add("Free Services");
        Services_names.add("Professional");
        Services_names.add("Technical");

        new stateExecuteTask().execute();

        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
                    if(disttList.size() !=0)
                    {
                        ChooseDistt.clear();
                        disttAdapter.notifyDataSetChanged();
                        spin_distt.setAdapter(null);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < StateHashMap.size(); i++)
                {

                    if (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position)))
                    {

                        new DisttExecuteTask(StateHashMap.get(i).getState_id()).execute();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //*************************************************************

                 final ListAdapter listAdapter_simple = new ArrayAdapter<String>(Service_State_Distt_Reg_Activity.this, android.R.layout.simple_spinner_item, Services_names);
//                             ArrayAdapter<String> dataAdapter = new
                 // Drop down layout style - list view with radio button
                 ((ArrayAdapter) listAdapter_simple).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                 // attaching data adapter to spinner
                 service_type_spinner.setAdapter((SpinnerAdapter) listAdapter_simple);
                 service_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                   service_type_spinner.setBackgroundColor(Color.parseColor("#000000"));
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {

                     }
                 });
         }


             public class stateExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String sever_url = "http://heightsmegamart.com/CPEPSI/Api/get_state";
           // String sever_url = "http://paramgoa.com/cpepsi/Api/get_state";
            //+ AppPreference.getUserid(AddStudentActivity.this);


            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {
//                        ChooseState.add("Select State");

                        JSONArray jsonArray = object.getJSONArray("state");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state_id = jsonObject1.getString("state_id");
                            String state_name = jsonObject1.getString("state_name");
                            stateList.add(new StateModel(state_id, state_name));
                            StateHashMap.put(i, new StateModel(state_id,state_name));
                            ChooseState.add(state_name);

                            // if (StateHashMap.)

                        }

                        stateAdapter = new ArrayAdapter<String>(Service_State_Distt_Reg_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_state.setAdapter(stateAdapter);


                    }else {
                        Toast.makeText(Service_State_Distt_Reg_Activity.this, "no state found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class DisttExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";

        String strStateId;

        public DisttExecuteTask(String strSid) {
            this.strStateId=strSid;
        }

        @Override
        protected String doInBackground(String... params) {
            String sever_url = "http://heightsmegamart.com/CPEPSI/Api/get_district?state_id="+strStateId;
          //  String sever_url = "http://paramgoa.com/cpepsi/Api/get_district?state_id="+strStateId;

            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {

                       // ChooseDistt.add("Select District");
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state_id = jsonObject1.getString("state_id");
                            String state_name = jsonObject1.getString("state_name");


                            disttList.add(new StateModel(state_id, state_name));
                            // StateHashMap.put(state_id, new StateModel(state_id,state_name));
                            ChooseDistt.add(state_name);

                        }

                        disttAdapter = new ArrayAdapter<String>(Service_State_Distt_Reg_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseDistt);
                        disttAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_distt.setAdapter(disttAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(Service_State_Distt_Reg_Activity.this, "no state found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
