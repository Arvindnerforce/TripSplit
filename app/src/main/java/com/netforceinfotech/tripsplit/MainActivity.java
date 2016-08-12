package com.netforceinfotech.tripsplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.netforceinfotech.tripsplit.Profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    Button sign_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sign_button = (Button) findViewById(R.id.sign_button);

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);

                startActivity(i);
            }
        });

    }
}
