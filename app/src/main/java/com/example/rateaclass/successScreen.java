package com.example.rateaclass;

/*
* This is our success activity that is shown to the user when their review is successfully submitted
*/

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class successScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_review);
        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                returnToMain();
                finish();
            }
        });
    }

    public void returnToMain()
    {
        Intent intent = new Intent(this, startUpScreen.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
