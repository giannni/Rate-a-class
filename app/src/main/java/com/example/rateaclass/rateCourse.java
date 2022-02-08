package com.example.rateaclass;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class rateCourse extends AppCompatActivity
{
    //Data input from users
    EditText professor, comments;
    Spinner courseName, courseNumber;
    RatingBar rating;
    //variables to use for inserting to SQL
    String course_name, professor_name, comments_given, course_number, rating_value;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_course);
        comments = findViewById(R.id.comments);
        professor = findViewById(R.id.professorName);
        courseNumber = findViewById(R.id.courseNumber);
        courseName = findViewById(R.id.courseName);
        rating = findViewById(R.id.ratingBar);
        Button submit = findViewById(R.id.submitButton);

        comments.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    // Just ignore the [Enter] key
                    return true;
                }
                // Handle all other keys in the default way
                return (keyCode == KeyEvent.KEYCODE_ENTER);
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //called when we click the submit button
                submitRating(v);
                finish();
            }
        });

        courseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //method is called when the user selects something from the first spinner on the rating page
                //listener is used to check the first spinner, not the second one that we want to populate
                courseSelection();
                courseName.setOnItemSelectedListener(this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void submitRating(View v)
    {
        //get our inputted data to execute in the params for the php script
        course_name = String.valueOf(courseName.getSelectedItem());
        professor_name = professor.getText().toString();
        comments_given = comments.getText().toString();
        course_number = String.valueOf(courseNumber.getSelectedItem());
        rating_value = String.valueOf(rating.getRating());
        backgroundTask background = new backgroundTask();
        background.execute(rating_value, course_name, course_number, comments_given, professor_name);
    }

    public void goToSuccessActivity()
    {
        //if post is successful, call this function to go to success screen activity
        Intent intent = new Intent(this, successScreen.class);
        startActivity(intent);
    }

    class backgroundTask extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {
            //create parameters for the post execute when the user hits submit
            String professor = params[4];
            String comments = params[3];
            String courseName = params[1];
            String courseNumber = params[2];
            String rating = params[0];
            String data = "";
            int tmp;

            try
            {
                //url that has the php file with database connection and inserts data
                //doesn't support HTTPS as this isn't about security, rather than functionality(pretty sure i had to force it to use clear text?)
                URL url = new URL("http://192.168.1.2/api/insert.php");
                //url parameters that get added on to the url to POST
                String urlParams = "rating="+rating+"&name="+courseName+"&number="+courseNumber+"&comments="+comments+"&professor="+professor;

                //open a http connection to our url
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream output = httpURLConnection.getOutputStream();
                output.write(urlParams.getBytes());
                output.flush();
                output.close();
                InputStream input = httpURLConnection.getInputStream();
                while((tmp = input.read())!=-1)
                {
                    data += (char)tmp;
                }
                input.close();
                httpURLConnection.disconnect();
                return data;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals(" "))
            {
                s = "Rating submitted!";
            }
            //set custom toast message(wont change though?)
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            // go to success activity
            goToSuccessActivity();
        }
    }

    public void courseSelection()
    {
         /*
        check what course name is selected in the course spinner
        and display course numbers in the course spinner that correspond to it
        */
        ArrayAdapter<CharSequence> adapter = null;
        String currentSelection = courseName.getSelectedItem().toString();
        if (currentSelection.equals("Accounting"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.acct, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Computer Science"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.cs, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Art") || currentSelection.equals("Art Education"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.art, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Astronomy"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.astr, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Biology"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.bio, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Business Administration"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.busa, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Chemistry"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.chem, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Communication Arts"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.ca, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Criminal Justice"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.cj, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Economics"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.econ, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.contains("Education"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.ed, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("English"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.eng, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Finance"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.fin, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Forensic Science"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.fs, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("French"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.fr, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Geography"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.geog, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("History"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.hist, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Italian"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.ital, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Kinesiology"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.kin, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Management"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.mgt, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Marketing"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.mkt, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Mathematics"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.math, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Music"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.mus, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Philosophy"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.phil, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Physics"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.phy, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Political Science"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.pols, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Psychology"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.psyc, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Religious Studies"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.rels, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Science"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.sci, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Sociology"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.soc, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Spanish"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.span, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Writing"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.writing, android.R.layout.simple_spinner_item);
        }
        else if(currentSelection.equals("Hospitality Management"))
        {
            adapter = ArrayAdapter.createFromResource(this, R.array.hosp, android.R.layout.simple_spinner_item);
        }

        if(adapter != null) {
            adapter.notifyDataSetChanged();
            courseNumber.setAdapter(adapter);
        }
    }

}
