package com.example.rateaclass;

/*
* This handles parsing our json data into an object from our database
* This also creates our searchview item that will handle filtering out the data based on course number
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class viewCourse extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
    private RatingAdapter ratingAdapter;
    private ArrayList<Rating> mRatingList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        getSupportActionBar().setTitle("Search for a course");
        mRecyclerView = findViewById(R.id.reviewViews);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRatingList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON()
    {
        //our url that outputs the json data from the database
        String url = "http://192.168.1.2/api/retrieve.php";
        //create a GET request from our url and try to get the data
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            //get the data from the json object named result
                            JSONArray jsonArray = response.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                //get items in the array from the JSON Object
                                String name = hit.getString("name");
                                String number = hit.getString("number");
                                String professor = hit.getString("professor");
                                String comments = hit.getString("comments");
                                String rating = hit.getString("rating");
                                //add a new rating object to our recycleview list
                                mRatingList.add(new Rating(rating, name, number, comments, professor));
                            }
                                Collections.sort(mRatingList, new Comparator<Rating>()
                                {
                                @Override
                                public int compare(Rating o1, Rating o2)
                                {
                                    return o1.getCourseName().compareToIgnoreCase(o2.getCourseName());
                                }
                            });
                            ratingAdapter = new RatingAdapter(viewCourse.this, mRatingList);
                            mRecyclerView.setAdapter(ratingAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this is used to add our search option in the action bar of the layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        //this is our searchview item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search... (Course Number)");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //set a listener to listen for input to the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter our recycleview with our search term that the user enters
                ratingAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}