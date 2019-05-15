package com.provider.admin.cpepsi_provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Adapters.MyAdapter;
import com.provider.admin.cpepsi_provider.Adapters.PagerView;
import com.provider.admin.cpepsi_provider.Adapters.RequestAdapter;
import com.provider.admin.cpepsi_provider.Model.RequestModel;
import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.SharedPreferences.SessionManager;
import com.provider.admin.cpepsi_provider.Util.Connectivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.Attributes;

import me.relex.circleindicator.CircleIndicator;


public class Home_navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TabLayout.BaseOnTabSelectedListener {
    TextView text_1_comp;
    SessionManager manager;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.one, R.drawable.four};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String Name, Email,Serv,SubService;
    TextView nav_name, nav_email,nav_service,nav_subservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Name = AppPreference.getNamee(this);
        Email = AppPreference.getEmail(this);
        Serv = AppPreference.getService(this);
        SubService = AppPreference.getSubservice(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        // Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Accept"));
        tabLayout.addTab(tabLayout.newTab().setText("Decline"));
        tabLayout.addTab(tabLayout.newTab().setText("Complete"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager1);
        // Creating our pager adapter
        PagerView adapter = new PagerView(getSupportFragmentManager(), tabLayout.getTabCount());
        // Adding adapter to pager
        viewPager.setAdapter(adapter);
        //  Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        // tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

//*******************************************************************************

        manager = new SessionManager(this);

        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        nav_name = (TextView) header.findViewById(R.id.nav_name);
        nav_email = (TextView) header.findViewById(R.id.nav_email);
        nav_service = (TextView) header.findViewById(R.id.nav_service);
        nav_subservice = (TextView) header.findViewById(R.id.nav_subservice);
        nav_name.setText(Name);
        nav_email.setText(Email);
        nav_service.setText(Serv);
        nav_subservice.setText(" "+"("+SubService+")");


        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(Home_navigation.this, Profile_Completion.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Home_navigation.this,Reviews.class);
            startActivity(intent);

        }else if (id == R.id.nav_terms) {
            Intent intent = new Intent(Home_navigation.this,TermsConditions.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
          /*  Intent intent = new Intent(Home_navigation.this,FeedbackActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            manager.logoutUser();
            AppPreference.setId(getApplicationContext(), "null");
            manager.setService_selected("null");
            Intent intent = new Intent(Home_navigation.this, LOG_in_Service_provider.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //------------------------------------------------

    private void init() {
        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(Home_navigation.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3500, 3500);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    //---------------------------------------------------------
//    public class PostRequest extends AsyncTask<String, Void, String> {
//        ProgressDialog dialog;
//
//        protected void onPreExecute() {
//            dialog = new ProgressDialog(Home_navigation.this);
//            dialog.show();
//
//        }
//
//        protected String doInBackground(String... arg0) {
//
//            try {
//
//                URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_list");
//
//                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("id", AppPreference.getId(Home_navigation.this));
//
//                Log.e("postDataParams", postDataParams.toString());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000 /* milliseconds*/);
//                conn.setConnectTimeout(15000  /*milliseconds*/);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataString(postDataParams));
//
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    BufferedReader in = new BufferedReader(new
//                            InputStreamReader(
//                            conn.getInputStream()));
//
//                    StringBuffer sb = new StringBuffer("");
//                    String line = "";
//
//                    while ((line = in.readLine()) != null) {
//
//                        StringBuffer Ss = sb.append(line);
//                        Log.e("Ss", Ss.toString());
//                        sb.append(line);
//                        break;
//                    }
//
//                    in.close();
//                    return sb.toString();
//
//                } else {
//                    return new String("false : " + responseCode);
//                }
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result != null) {
//                dialog.dismiss();
//
//                JSONObject jsonObject = null;
//                String s = result.toString();
//                try {
//                    jsonObject = new JSONObject(result);
//                    String responce = jsonObject.getString("responce");
//                    JSONArray dataArray = jsonObject.getJSONArray("data");
//                    for (int i=0; i<dataArray.length(); i++){
//                        JSONObject dataObj = dataArray.getJSONObject(i);
//                        String pr_id = dataObj.getString("pr_id");
//                        String customer_id = dataObj.getString("customer_id");
//                        String provider_id = dataObj.getString("provider_id");
//                        String discription = dataObj.getString("discription");
//                        String Prostatus = dataObj.getString("Prostatus");
//                        String id = dataObj.getString("id");
//                        name = dataObj.getString("name");
//                        String mobile = dataObj.getString("mobile");
//                        email = dataObj.getString("email");
//                        String contact = dataObj.getString("contact");
//                        String password = dataObj.getString("password");
//                        String status = dataObj.getString("status");
//                        reqt_list.add(new RequestModel(pr_id,customer_id,provider_id,discription,Prostatus,id,name,mobile,
//                                email,contact,password,status));
//                    }
//
//                    requestAdapter = new RequestAdapter(Home_navigation.this, reqt_list);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Home_navigation.this);
//                    recyclerRequest.setLayoutManager(mLayoutManager);
//                    recyclerRequest.setItemAnimator(new DefaultItemAnimator());
//                    recyclerRequest.setAdapter(requestAdapter);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public String getPostDataString(JSONObject params) throws Exception {
//
//            StringBuilder result = new StringBuilder();
//            boolean first = true;
//
//            Iterator<String> itr = params.keys();
//
//            while (itr.hasNext()) {
//
//                String key = itr.next();
//                Object value = params.get(key);
//
//                if (first)
//                    first = false;
//                else
//                    result.append("&");
//
//                result.append(URLEncoder.encode(key, "UTF-8"));
//                result.append("=");
//                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//            }
//            return result.toString();
//        }
//    }
    //---------------------------------------------------------
}
