package com.provider.admin.cpepsi_provider.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Adapters.RequestAdapter;
import com.provider.admin.cpepsi_provider.Model.RequestModel;
import com.provider.admin.cpepsi_provider.R;
import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.Util.Connectivity;

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

import javax.net.ssl.HttpsURLConnection;

public class decline_fragment extends Fragment  {

    RecyclerView recyclerRequest;
    ArrayList<RequestModel> reqt_list=new ArrayList<RequestModel>();
    private RequestAdapter requestAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View allView = inflater.inflate(R.layout.fragment_decline, container, false);



        recyclerRequest = (RecyclerView)allView.findViewById(R.id.recyclerRequest);

        if (Connectivity.isNetworkAvailable(getContext())){
            new PostRequest().execute();
        }else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }


//************************************************************************************
        return allView;
    }

    public class PostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_list");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_list");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id", AppPreference.getId(getContext()));

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
                            String pr_id = dataObj.getString("pr_id");
                            String customer_id = dataObj.getString("customer_id");
                            String provider_id = dataObj.getString("provider_id");
                            String discription = dataObj.getString("discription");
                            String date = dataObj.getString("date");
                            String Prostatus = dataObj.getString("Prostatus");
                            String id = dataObj.getString("id");
                            String name = dataObj.getString("name");
                            String email = dataObj.getString("email");
                            String contact = dataObj.getString("contact");
                            String password = dataObj.getString("password");
                            String address = dataObj.getString("address");
                            String status = dataObj.getString("status");
                            String payment_status = dataObj.getString("payment_status");
                            String payment_amount = dataObj.getString("payment_amount");
                            String image = dataObj.getString("image");
                            if (Prostatus.equals("2")) {
                                reqt_list.add(new RequestModel(pr_id, customer_id, provider_id, discription,date, Prostatus, id, name,
                                        email, contact, password,address, status,payment_status,payment_amount,image));
                            }
                        }

                        requestAdapter = new RequestAdapter(getContext(), reqt_list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerRequest.setLayoutManager(mLayoutManager);
                        recyclerRequest.setItemAnimator(new DefaultItemAnimator());
                        recyclerRequest.setAdapter(requestAdapter);

                    } else {
                        // txtresult.setText("Oops! No Data.");
                        Toast.makeText(getActivity(), "Oops! No Data.", Toast.LENGTH_LONG).show();
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
//************************************