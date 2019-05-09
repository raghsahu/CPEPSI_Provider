package com.provider.admin.cpepsi_provider.Adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.provider.admin.cpepsi_provider.Java_files.Services;
import com.provider.admin.cpepsi_provider.R;
import com.provider.admin.cpepsi_provider.Service_provider_reg;
import com.provider.admin.cpepsi_provider.SharedPreferences.SessionManager;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.provider.admin.cpepsi_provider.Service_provider_reg.service_id_dx;
import static com.provider.admin.cpepsi_provider.Service_provider_reg.services_provider_spinner;
import static com.provider.admin.cpepsi_provider.Service_provider_reg.sub_services_drop;

public class Service_Recycler_Adapter extends RecyclerView.Adapter<Service_Recycler_Adapter.MyViewHolder> {
    private Context mContext;
    DownloadManager downloadManager;
    private Service_Recycler_Adapter service_recycler_adapter;
    HashMap<Integer, String> subcatigoriesMap = new HashMap<>();
    URL image_download_url;
    SessionManager sessionManager;
    int size_of_services = 0;
    int pos_try;
    public MyViewHolder holder;

    DownloadManager.Request request;
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();


    String service;

    public List<Services> services_list;
    private ProgressDialog progressBar;


//    public Rec_Reports_adapter(Context gallery_act, List<Reports> Doc) {
//        mContext = gallery_act;
//      this.Doc = Doc;
//    }


    public Service_Recycler_Adapter(Context c, List<Services> services) {
        mContext = c;
        this.services_list = services;

        setHasStableIds(true);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView service_id;
        public TextView service_doc;


        public MyViewHolder(View itemView) {
            super(itemView);
            service_doc = (TextView) itemView.findViewById(R.id.service_txt_id);
            service_id = (TextView) itemView.findViewById(R.id.service_id);
//            email_to = (TextView) itemView.findViewById(R.id.email_to);
//            doc_date = (TextView)itemView.findViewById(R.id.doc_date);


            //   new Get_The_Selected().execute();


        }
    }

//    public Rec_Reports_adapter(List<Reports> reportss) {
//        this.Doc = reportss;
//    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list, parent, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        this.holder = holder;

        this.pos_try = position;
        final Services services1;
        services1 = services_list.get(pos_try);
        Log.e("Position", "is " + pos_try);
        service = services1.getService_doc();
        StrictMode.setVmPolicy(builder.build());
        //holder.name_of_doc.setText(reports.getReport_Doc());
        holder.service_doc.setText(services1.getService_doc());
        holder.service_id.setText(Integer.toString(services1.getservice_id()));
        // Service_alert.show();

        holder.service_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Service_alert.isShowing() && Service_alert !=null){
//                    Service_alert.dismiss();
//                }
                int s = services1.getservice_id();
                Toast.makeText(v.getContext(), "sdgsdfsd" + services1.getService_doc(), Toast.LENGTH_LONG).show();

                Intent to_payback = new Intent(v.getContext(), Service_provider_reg.class);
                services_provider_spinner.setText("" + services1.getService_doc());
                service_id_dx.setText("" + services1.getservice_id());
                sessionManager = new SessionManager(mContext);
                sessionManager.setService_selected(services1.getService_doc());
                sessionManager.setSERVICE_SUB_KEY(Integer.parseInt(service_id_dx.getText().toString()));
                services1.setservice_id(Integer.parseInt("" + services1.getservice_id()));


                //    new Get_The_Selected(v.getContext() ,services1.getservice_id()).execute();
                to_payback.putExtra("selected_ser_name", services1.getService_doc());
                to_payback.putExtra("selected_ser_id", services1.getservice_id());

//                Intent to_get_service_provider = new Intent(v.getContext() , GET_Service_providers.class);
                v.getContext().startActivity(to_payback);
                ((Activity) v.getContext()).finish();

            }
        });
//        holder.doc_date.setText(reports.getDoc_date());

        // notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return services_list.size();
    }


    @Override
    public long getItemId(int position) {
//        return super.getItemId(position);
        return position;
    }

    private class Get_The_Selected extends AsyncTask<String, Void, String> {
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

        public Get_The_Selected(Context context, int service_did) {
            this.main_context = context;
            this.ser_id_int = service_did;
        }

        @Override
        protected void onPreExecute() {
            Service_bar = new ProgressDialog(main_context);
            Service_bar.show();
            Service_bar.setCancelable(false);

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            JSONObject postDataParams = new JSONObject();


            try {
                url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");
              //  url = new URL("https://www.paramgoa.com/cpepsi/api/Get_SubServices");
                postDataParams.put("Servicesid", ser_id_int);


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

                            serv_string_name = Service_json_object.getString("ServiceSubCategory");
                            subcatigoriesMap.put(actual_id, serv_string_name);
//                            serviceList.add(Service_object.getString("Service"));
//                            service_adapter = new Service_adapters(getActivity() , serviceList);
//                            Services services = new Services(serv_string);
//                            serviceList.add(services);
//                            serviceList.add(services);
                            Sub_Services_provided.add(serv_string_name);


//                            if(type_of_service_json ==0){
//                                Services_names.add(serv_string_name);
//                            }else{
//                                Services_provided.add(serv_string_name);
//                            }


                            size_of_services++;


                        }
                        if (size_of_services >= jsonArray.length()) {

                            Log.e("size_of", "json is" + size_of_services);
                            if (service_id_txt == 0 || service_id_txt == 1 || service_id_txt == 2) {
                                listAdapter_sub_service_names = new ArrayAdapter<String>(main_context, android.R.layout.simple_spinner_item, Sub_Services_provided);
                                (listAdapter_sub_service_names).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
//                                services_provider_spinner.setAdapter( listAdapter_service_names_pro);
                                sub_services_drop.setAdapter(listAdapter_sub_service_names);

//                            size_of_services =0;
                                Log.e("now size_of", "json is" + size_of_services);
//                            }

                            }

                        }


//                        Service_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, R.id.service_txt_id, Service_names);

//                        ser_list_view.deferNotifyDataSetChanged();

                    } else {
                        Sub_Services_Null_Provided.add("None found");
                        Adapter_sub_service_name_null = new ArrayAdapter<String>(main_context, android.R.layout.simple_spinner_item, Sub_Services_Null_Provided);
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


    }
}