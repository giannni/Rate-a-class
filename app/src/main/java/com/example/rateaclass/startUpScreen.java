package com.example.rateaclass;

/*
* This is our first activity that shows up on the app when it is launched
* This handles just switching between the two activities that are given to the user
*/

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        Button rateButton = findViewById(R.id.rateCourse);
        Button viewButton = findViewById(R.id.viewCourse);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRateCourse();
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewCourse();
            }
        });
    }

    public void openViewCourse()
    {
        Intent intent = new Intent(this, viewCourse.class);
        startActivity(intent);
    }

    public void openRateCourse()
    {
        Intent intent = new Intent(this, rateCourse.class);
        startActivity(intent);
    }
}


