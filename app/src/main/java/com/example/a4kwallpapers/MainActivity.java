package com.example.a4kwallpapers;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText searchEdTxt;
    ImageView searchBtn;
    CardView trending,nature,technology,programing;
    RecyclerView myRcv;
    WallpaperAdapter wallpaperAdapter;
    ArrayList<WallpaperModel> wallpaperArrayList;
    ProgressBar progressBar;

    // using for none stop scrolling
    int pageNumber = 1;
    Boolean scrolling = false;
    int currentItem , totalItem , scrollOutItem;

//    String BASE_URL="https://api.pexels.com/v1/curated?page="+pageNumber+"&per_page=88";
    String BASE_URL="https://api.pexels.com/v1/curated?page="+pageNumber+"&per_page=88";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.grey));



        searchEdTxt = findViewById(R.id.ed_txt_search);
        searchBtn = findViewById(R.id.search_btn);
        technology = findViewById(R.id.cd_technology);
        nature = findViewById(R.id.cd_nature);
        trending = findViewById(R.id.cd_trending);
        programing = findViewById(R.id.cd_programing);
        myRcv = findViewById(R.id.rcv);
        progressBar = findViewById(R.id.loading_prog);

        wallpaperArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this , 2);
        myRcv.setLayoutManager(gridLayoutManager);
//        myRcv.setLayoutManager(new GridLayoutManager(this , 2));
        myRcv.setHasFixedSize(true);

        wallpaperAdapter = new WallpaperAdapter(MainActivity.this  , wallpaperArrayList);
        myRcv.setAdapter(wallpaperAdapter);


//         for none stop scrolling images
        myRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    scrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (scrolling && currentItem+scrollOutItem == totalItem){
                    scrolling = false;
                    fetchWallpapers();
                }
            }
        });

        fetchWallpapers();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEdTxt.getText().toString().trim().toLowerCase();

                if (query.isEmpty()){
                    Toast.makeText(MainActivity.this, "Search any thing", Toast.LENGTH_SHORT).show();
                }
                else {
                    searchImage(query);
                }

            }
        });

        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperArrayList.clear();
                fetchWallpapers();
            }
        });
        nature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperArrayList.clear();
                String query = "nature";
                searchImage(query);

            }
        });
        technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperArrayList.clear();
                String query = "technology and programming";
                searchImage(query);

            }
        });
        programing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperArrayList.clear();
                String query = "animal";
                searchImage(query);
            }
        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.wall_toolbar , menu);
//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchImage(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                searchImage(query);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }


//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case R.id.search:
//
//
//                break;
//        }
//
//        return super.onContextItemSelected(item);
//    }

    private void searchImage(String query) {


        progressBar.setVisibility(View.VISIBLE);

        wallpaperArrayList.clear();

//        String url = "https://api.pexels.com/v1/search?query="+query+"?page="+pageNumber+"&per_page=88";
        String url = "https://api.pexels.com/v1/search?query="+query+"&per_page=88";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONArray photoArray = response.getJSONArray("photos");

                            for (int i=0 ; i<photoArray.length(); i++){

                                JSONObject photoObject = photoArray.getJSONObject(i);

                                JSONObject object = photoObject.getJSONObject("src");
                                String imgPortrait = object.getString("portrait");

                                WallpaperModel wallpaperModel = new WallpaperModel(imgPortrait);
                                wallpaperArrayList.add(wallpaperModel);

                            }
                            wallpaperAdapter.notifyDataSetChanged();
//                            pageNumber++;

                        }catch (JSONException e){

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Wallpaper Loading Failed", Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String , String> header = new HashMap<>();
                header.put("Authorization" , "563492ad6f917000010000018299bf8a7063449795d0635984d8b5c6");
                return header;
            }
        };
        queue.add(jsonObjectRequest);


    }

    private void fetchWallpapers(){

        progressBar.setVisibility(View.VISIBLE);

//        wallpaperArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONArray photoArray = response.getJSONArray("photos");

                            for (int i=0 ; i<photoArray.length(); i++){

                                JSONObject photoObject = photoArray.getJSONObject(i);

                                JSONObject object = photoObject.getJSONObject("src");
                                String imgPortrait = object.getString("portrait");

                                WallpaperModel wallpaperModel = new WallpaperModel(imgPortrait);
                                wallpaperArrayList.add(wallpaperModel);

                            }
                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        }catch (JSONException e){

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Wallpaper Loading Failed", Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String , String> header = new HashMap<>();
                header.put("Authorization" , "563492ad6f917000010000018299bf8a7063449795d0635984d8b5c6");
                return header;
            }
        };
        queue.add(jsonObjectRequest);

    }

}