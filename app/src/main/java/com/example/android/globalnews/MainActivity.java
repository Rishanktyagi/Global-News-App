package com.example.android.globalnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{
    //ebcd1f4965b84829b37a32560a9b004b

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter= new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All",
                "https://images.unsplash.com/photo-1621711206909-ec52405a4e1b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTd8fGFsbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology",
                "https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science",
                "https://media.istockphoto.com/photos/data-scientists-male-programmer-using-laptop-analyzing-and-developing-picture-id1295900106?b=1&k=20&m=1295900106&s=170667a&w=0&h=kQ2UWilU4Bild5aP03CaF65gMbSI-chG--7dd2oS8GM="));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports",
                "https://media.istockphoto.com/photos/various-sport-equipments-on-grass-picture-id949190736?b=1&k=20&m=949190736&s=170667a&w=0&h=f3ofVqhbmg2XSVOa3dqmvGtHc4VLA_rtbboRGaC8eNo="));
        categoryRVModalArrayList.add(new CategoryRVModal("General",
                "https://images.unsplash.com/photo-1432821596592-e2c18b78144f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Z2VuZXJhbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Business",
                "https://images.unsplash.com/photo-1641179497501-6c9dff21382d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDIwfGFldTZyTC1qNmV3fHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment",
                "https://media.istockphoto.com/photos/popcorn-and-clapperboard-picture-id1191001701?b=1&k=20&m=1191001701&s=170667a&w=0&h=uVqDpnXNtnfbhB-F4sWac_t3oL_YSrDuHeCKdaJGS3U="));
        categoryRVModalArrayList.add(new CategoryRVModal("Health",
                "https://images.unsplash.com/photo-1605819048164-484f3578f959?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=ebcd1f4965b84829b37a32560a9b004b";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=ebcd1f4965b84829b37a32560a9b004b";
        String BASE_URL="https://newsapi.org/";
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call= retrofitAPI.getAllNews(url);
        }else{
            call=retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles= newsModal.getArticles();
                for (int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get News", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category= categoryRVModalArrayList.get(position).getCategory();
        getNews(category);

    }
}