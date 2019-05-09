package com.provider.admin.cpepsi_provider;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static com.provider.admin.cpepsi_provider.Service_State_Distt_Reg_Activity.Spin_distt;
import static com.provider.admin.cpepsi_provider.Service_State_Distt_Reg_Activity.Spin_state;
//import static com.provider.admin.cpepsi_provider.Service_provider_reg.Address;

public class Profession_As_Provider extends AppCompatActivity {
    //Getting values from intent
    String type_spinner_val, name_of_firm_val, nature_of_firm_val, contact_no_service_val, contact_of_service_val,
            email_of_service_val, full_Address, state, place, Service, Sub_service;
    Button sbmt_pro;
    TextView text_reg;
    CheckBox check_per;
    EditText first_name_provider, contact_no_provider, email_id_provider, pass_of_provider,business
            ,cityc,statee,placee,dateob,adharNumber;
    Calendar myCalendar;
    ProgressDialog dialog;
    DatePickerDialog.OnDateSetListener date;
    private String dateFlage;
    private String strDateDevice;
    private String strDateServer;

    private String beforeText, currentText;
    private boolean noAction, addStroke, dontAddChar, deleteStroke;
    public static String res;
    String  State, Distt;
    private String responce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession__as__provider);

        sbmt_pro = (Button) findViewById(R.id.sbmt_pro);
        check_per = (CheckBox) findViewById(R.id.check_per);
        text_reg = findViewById(R.id.text_reg);
        first_name_provider = findViewById(R.id.first_name_provider);
       // mid_of_provider = findViewById(R.id.mid_of_provider);
        contact_no_provider = findViewById(R.id.contact_no_provider);
        email_id_provider = findViewById(R.id.email_id_provider);
        pass_of_provider = findViewById(R.id.pass_of_provider);
        business = findViewById(R.id.business);
        cityc = findViewById(R.id.cityc);
        cityc.setText(Spin_distt);
        statee = findViewById(R.id.statee);
        statee.setText(Spin_state);

        placee = findViewById(R.id.placee);
       // placee.setText(Address);
        dateob = findViewById(R.id.dateob);
        adharNumber = findViewById(R.id.adharNumber);
        //getting valuse form intent++++++++++++++++++++++++++
        adharNumber.addTextChangedListener(new TextWatcher(){
           // private EditText creditCard;


            public void afterTextChanged(Editable s) {
                if (addStroke) {
                    Log.i("TextWatcherImplement", "afterTextChanged String == " + s + " beforeText == " + beforeText + " currentText == " + currentText);
                    noAction = true;
                    addStroke = false;
                    adharNumber.setText(currentText + "-");
                } else if (dontAddChar) {
                    dontAddChar = false;
                    noAction = true;
                    adharNumber.setText(beforeText);
                } else if (deleteStroke) {
                    deleteStroke = false;
                    noAction = true;
                    currentText = currentText.substring(0, currentText.length() - 1);
                    adharNumber.setText(currentText);
                } else {
                    noAction = false;
                    adharNumber.setSelection(adharNumber.getText().length()); // set cursor at the end of the line.
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                Log.i("TextWatcherImplement", "beforeTextChanged start==" + String.valueOf(start) + " count==" + String.valueOf(count) + " after==" + String.valueOf(after));
                if (start >= 14)
                    beforeText = s.toString();

            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.i("TextWatcherImplement", "onTextChanged start==" + String.valueOf(start) + " before==" + String.valueOf(before) + " count==" + String.valueOf(count) + " noAction ==" + String.valueOf(noAction));
                if ( (before < count) && !noAction ) {
                    if ( (start == 3) || (start == 8)  ) {
                        currentText = s.toString();
                        addStroke = true;
                    } else if (start >= 14) {
                        currentText = s.toString();
                        dontAddChar = true;
                    }
                } else {
                    if ( (start == 4) ||  (start == 9)  ) { //(start == 5) || (start == 10) || (start == 15)
                        currentText = s.toString();
                        deleteStroke = true;
                    }
                }
            }
        });


        picDate();

        type_spinner_val = getIntent().getStringExtra("type_spinner");
        Service = getIntent().getStringExtra("service_in");
        Sub_service = getIntent().getStringExtra("sub_service_in");
        try{
            if(!Sub_service.isEmpty())
            {

            }
        }catch (Exception e)
        {
            Sub_service = "xxx";
            e.printStackTrace();
        }

        name_of_firm_val = getIntent().getStringExtra("name_of_firm");
        nature_of_firm_val = getIntent().getStringExtra("nature_of_firm");
        contact_no_service_val = getIntent().getStringExtra("contact_no_service");
        email_of_service_val = getIntent().getStringExtra("email_of_service");
        contact_of_service_val = getIntent().getStringExtra("contact_of_service");
        full_Address = getIntent().getStringExtra("loc_i");
        placee.setText(full_Address);
        state = getIntent().getStringExtra("email_city_of_pro_service");
        place = getIntent().getStringExtra("state_of_pro_i");
//        contact_of_service_val = getIntent().getStringExtra("type_spinner")

        text_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Profession_As_Provider.this,Terms_Viewer.class);
                startActivity(intent);

            }
        });

        sbmt_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_per.isChecked()) {
                    if (!first_name_provider.getText().toString().isEmpty()  &&
                            !email_id_provider.getText().toString().isEmpty() && !pass_of_provider.getText().toString().isEmpty() && !cityc.getText().toString().isEmpty()
                            && !statee.getText().toString().isEmpty() && !placee.getText().toString().isEmpty()&& !business .getText().toString().isEmpty()) {
                        //calling api
                        if (have_a_check_on_email(email_id_provider.getText().toString())) {
                            new Register_The_Provider(v).execute();
                        } else {
                            email_id_provider.setError("Not a valid email address");
                        }

                    } else {
                        if (first_name_provider.getText().toString().isEmpty()) {
                            first_name_provider.setError("This field can not be empty");
                        }
//                        if (mid_of_provider.getText().toString().isEmpty()) {
//                            mid_of_provider.setError("This field can not be empty");
//                        }
//                        if (surname_of_provider.getText().toString().isEmpty()) {
//                            surname_of_provider.setError("This field can not be empty");
//                        }
                        if (email_id_provider.getText().toString().isEmpty()) {
                            email_id_provider.setError("This field can not be empty");
                        }
                        if (first_name_provider.getText().toString().isEmpty()) {
                            first_name_provider.setError("This field can not be empty");
                        }
                        if (pass_of_provider.getText().toString().isEmpty()) {
                            pass_of_provider.setError("This field can not be empty");
                        }
                        if (cityc.getText().toString().isEmpty()) {
                            cityc.setError("Please Enter Your City.");
                        }
                        if (statee.getText().toString().isEmpty()) {
                            statee.setError("Please Enter Your State.");
                        }
                        if (placee.getText().toString().isEmpty()) {
                            placee.setError("Please Enter Your Place.");
                        }
                        Toast.makeText(Profession_As_Provider.this, "Fields can not be null", Toast.LENGTH_SHORT).show();
                        Snackbar.make(v, "Fields can not be null", Toast.LENGTH_LONG).show();
                    }

                } else {
                    text_reg.setTextColor(Color.RED);
                    Toast.makeText(Profession_As_Provider.this, "Please check at the terms and condition box", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //---------------------------------------------

    private void picDate() {

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateFlage = "1";
                new DatePickerDialog(Profession_As_Provider.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        if (dateFlage.equalsIgnoreCase("1")) {
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
            dateob.setText(sdf.format(myCalendar.getTime()));
            dateob.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    //---------------------------------------------

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
           // Toast.makeText(this, "Ok valid", Toast.LENGTH_SHORT).show();
            return true;
        } else {
           // Toast.makeText(this, "Not ok", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private class Register_The_Provider extends AsyncTask<String, Void, String> {

        int service_id;
        ProgressDialog dialog;
        int size_of_services = 0;
        View v;

        public Register_The_Provider(View v) {
//            this.service_id = service_txt_id;
            this.v = v;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(v.getContext());
            dialog.show();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Ragistration");
             //   URL url = new URL("https://www.paramgoa.com/cpepsi/api/Ragistration");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("TypeofFirm", type_spinner_val);
                postDataParams.put("service", Service);
                postDataParams.put("sub_service", Sub_service);
                postDataParams.put("number", contact_no_provider.getText().toString());
                postDataParams.put("Designation", full_Address);
                postDataParams.put("City", cityc.getText().toString());
                postDataParams.put("state", statee.getText().toString());
                postDataParams.put("place", placee.getText().toString());
                postDataParams.put("name", first_name_provider.getText().toString());
                postDataParams.put("emailid", email_id_provider.getText().toString());
                postDataParams.put("password", pass_of_provider.getText().toString());
                postDataParams.put("business", "ngo");
                postDataParams.put("dob", dateob.getText().toString());
                postDataParams.put("adharno", adharNumber.getText().toString());


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
                   // boolean response = jsonObject.getBoolean("responce");
                   String msg = jsonObject.getString("massage");
                    responce = jsonObject.getString("responce");



                    if (msg.equals("OTP Sent Successfully")) {
                        Intent to_completion = new Intent(Profession_As_Provider.this, OtpActivity.class);
                       to_completion.putExtra("Otp",responce);
                        to_completion.putExtra("TypeofFirm", type_spinner_val);
                        to_completion.putExtra("service", Service);
                        to_completion.putExtra("sub_service", Sub_service);
                        to_completion.putExtra("number", contact_no_provider.getText().toString());
                        to_completion.putExtra("Designation", full_Address);
                        to_completion.putExtra("City", cityc.getText().toString());
                        to_completion.putExtra("state", statee.getText().toString());
                        to_completion.putExtra("place", placee.getText().toString());
                        to_completion.putExtra("name", first_name_provider.getText().toString());
                        to_completion.putExtra("emailid", email_id_provider.getText().toString());
                        to_completion.putExtra("password", pass_of_provider.getText().toString());
                        to_completion.putExtra("business", business.getText().toString());
                        to_completion.putExtra("dob", dateob.getText().toString());
                        to_completion.putExtra("adharno", adharNumber.getText().toString());


                        startActivity(to_completion);
                        finish();
                        //Snackbar.make(v, " " +msg, Toast.LENGTH_LONG).show();
                        Toast.makeText(Profession_As_Provider.this, ""+msg, Toast.LENGTH_SHORT).show();

////                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject Service_json_object = jsonArray.getJSONObject(i);
//                            String serv_string = Service_json_object.getString("Service");
////                            serviceList.add(Service_object.getString("Service"));
////                            service_adapter = new Service_adapters(getActivity() , serviceList);
////                            Services services = new Services(serv_string);
////                            serviceList.add(services);
////                            serviceList.add(services);
//
//                            size_of_services++;
//
//                        }
//                        if(size_of_services == jsonArray.length()) {
//
//                            //ser_list_view.setAdapter(service_recycler_adapter);
//                        }


//                        Service_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, R.id.service_txt_id, Service_names);

//                        ser_list_view.deferNotifyDataSetChanged();

                    } else {
                        Toast.makeText(Profession_As_Provider.this, "OTP Not Sent", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }
}
