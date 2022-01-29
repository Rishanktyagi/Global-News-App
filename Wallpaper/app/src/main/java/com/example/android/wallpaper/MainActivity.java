package com.example.android.wallpaper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.wallpaper.adapters.SuggestedAdapter;
import com.example.android.wallpaper.adapters.WallpaperAdapter;
import com.example.android.wallpaper.interfaces.RecyclerViewClickListener;
import com.example.android.wallpaper.models.SuggestedModel;
import com.example.android.wallpaper.models.WallpaperModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewClickListener {


    static final float END_SCALE =0.7f;
    ImageView menuIcon;
    LinearLayout contentView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RecyclerView recyclerView,topMostRecyclerView;
    RecyclerView.Adapter adapter;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    ArrayList<SuggestedModel>suggestedModels= new ArrayList<>();

    Boolean isScrolling=false;
    int currentItems,totalItems,scrollOutItems;

    TextView replaceTitle;

    ProgressBar progressBar;

    EditText searchEt;
    ImageView searchIv;

    int pageNumber=1;

    String url="https://api.pexels.com/v1/curated?page="+ pageNumber+"&per_page=80";// we need multiple pages when scrolling
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuIcon=findViewById(R.id.menu_icon);
        contentView=findViewById(R.id.content_view);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);

        navigationDrawer();
        //navigation drawer profile
        View headerView=navigationView.getHeaderView(0);
        ImageView appLogo=headerView.findViewById(R.id.app_image);

        recyclerView =findViewById(R.id.recyclerView);
        topMostRecyclerView =findViewById(R.id.suggestedRecyclerView);

        wallpaperModelList=new ArrayList<>();
        wallpaperAdapter=new WallpaperAdapter(this,wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdapter );


        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //scrolling behavior
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems =gridLayoutManager.getChildCount();
                totalItems =gridLayoutManager.getItemCount();
                scrollOutItems =gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling=false;
                    fetchWallpaper();
                }
            }
        });
        progressBar= findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        replaceTitle= findViewById(R.id.topMostTitle);

        fetchWallpaper();
        suggestedItems();

        //search edittext AND imageview
        searchEt=findViewById(R.id.searchEv);
        searchIv=findViewById(R.id.search_image);
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the functionality later
                Toast.makeText(MainActivity.this, "Search Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigationDrawer() {
        //navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //animation in the drawer
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //scale the view based on the current slide offset
                final float diffScaledOffset=slideOffset*(1-END_SCALE);
                final float offsetScale=1-diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the view accounting of the scaled width
                final float xOffset=drawerView.getWidth()*slideOffset;
                final float xOffsetDiff=contentView.getWidth()*diffScaledOffset/2;
                final float xTranslation=xOffset-xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_home:
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_trending:
                Toast.makeText(this, "Trending clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_most_viwed:
                Toast.makeText(this, "most viwed clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "about clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    private void suggestedItems(){
        topMostRecyclerView.setHasFixedSize(true);
        topMostRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        suggestedModels.add(new SuggestedModel(R.drawable.image2,"Trending"));
        suggestedModels.add(new SuggestedModel(R.drawable.image3,"Nature"));
        suggestedModels.add(new SuggestedModel(R.drawable.image4,"Architecture"));
        suggestedModels.add(new SuggestedModel(R.drawable.image5,"People"));
        suggestedModels.add(new SuggestedModel(R.drawable.image6,"Business"));

        adapter =new SuggestedAdapter(suggestedModels, MainActivity.this);
        topMostRecyclerView.setAdapter(adapter);
    }
    private void fetchWallpaper() {
        //fetch image url and name from the pexels api
        StringRequest request=new StringRequest(Request.Method.GET, url,
                response -> {

                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject =new JSONObject(response);
                        JSONArray jsonArray= jsonObject.getJSONArray("photos");

                        int length=jsonArray.length();

                        for(int i = 0; i <length; i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            int id=object.getInt("id");
                            String photographerName=object.getString("photographer");

                            JSONObject objectImage=object.getJSONObject("src");
                            String originalUrl =objectImage.getString("original");
                            String mediumUrl =objectImage.getString("medium");

                            WallpaperModel wallpaperModel=new WallpaperModel(id,originalUrl,mediumUrl,photographerName);
                            wallpaperModelList.add(wallpaperModel);
                        }

                         wallpaperAdapter.notifyDataSetChanged();
                         pageNumber++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String,String> params= new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001a37a7a13cd8046d3ac48a8f8d3bb5003");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @SuppressLint("SetTextIl8n")
    @Override
    public void onItemClick(int position) {
        ///we will work on it when we design the suggested recyclerview items
        progressBar.setVisibility(View.VISIBLE);
        if(position== 0){
            replaceTitle.setText("Trending");
            url ="https://api.pexels.com/v1/search/?page=" +pageNumber+ "&per_page=80&query=trending";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        }else if(position== 1){
            replaceTitle.setText("Nature");
            url ="https://api.pexels.com/v1/search/?page=" +pageNumber+ "&per_page=80&query=nature";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        }else if(position== 2){
            replaceTitle.setText("Architecture");
            url ="https://api.pexels.com/v1/search/?page=" +pageNumber+ "&per_page=80&query=architecture";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        }else if(position== 3){
            replaceTitle.setText("People");
            url ="https://api.pexels.com/v1/search/?page=" +pageNumber+ "&per_page=80&query=people";
            wallpaperModelList.clear();
            fetchWallpaper();  
            progressBar.setVisibility(View.GONE);
        }else if(position== 4){
            replaceTitle.setText("Business");
            url ="https://api.pexels.com/v1/search/?page=" +pageNumber+ "&per_page=80&query=business";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        }
    } 
}