package com.provider.admin.cpepsi_provider;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Adapters.RequestAdapter;
import com.provider.admin.cpepsi_provider.Adapters.ReviewsAdapter;
import com.provider.admin.cpepsi_provider.Model.RequestModel;
import com.provider.admin.cpepsi_provider.Model.ReviewsModel;
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

public class Reviews extends AppCompatActivity {
    RecyclerView reviewList;
    ArrayList<ReviewsModel> rew_list;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewList  = (RecyclerView)findViewById(R.id.reviewList);
    rew_list = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(this)){
         new PostReview().execute();
        }else {
            Toast.makeText(Reviews.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------------

    public class PostReview extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Reviews.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_feedbackByprovider");
            //    URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_feedbackByprovider");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("provider_id", AppPreference.getId(Reviews.this));

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
                            String customer_id = dataObj.getString("customer_id");
                            String provider_id = dataObj.getString("provider_id");
                            String name = dataObj.getString("name");
                            String email = dataObj.getString("email");
                            String contactno = dataObj.getString("contactno");
                            String address = dataObj.getString("address");
                            String feedbackservice = dataObj.getString("feedbackservice");
                                rew_list.add(new ReviewsModel(id, customer_id, provider_id, name, email, contactno, address, feedbackservice));
                        }

                        reviewsAdapter = new ReviewsAdapter(Reviews.this, rew_list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Reviews.this);
                        reviewList.setLayoutManager(mLayoutManager);
                        reviewList.setItemAnimator(new DefaultItemAnimator());
                        reviewList.setAdapter(reviewsAdapter);

                    } else {
                        // txtresult.setText("Oops! No Data.");
                        Toast.makeText(Reviews.this, "Oops! No Data.", Toast.LENGTH_LONG).show();
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
