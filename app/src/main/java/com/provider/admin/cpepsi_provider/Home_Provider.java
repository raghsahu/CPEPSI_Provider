package com.provider.admin.cpepsi_provider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Home_Provider extends AppCompatActivity {
    TextView text_1_comp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__provider);

       /* text_1_comp = findViewById(R.id.text_1_comp);
        text_1_comp.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent to_comp = new Intent(Home_Provider.this, Profile_Completion.class);
                                               startActivity(to_comp);

                                           }
                                       }
        );*/
    }
}
