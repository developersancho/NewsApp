package com.sf.newsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.sf.newsapp.adapter.ListNewsAdapter;
import com.sf.newsapp.common.Common;
import com.sf.newsapp.model.Article;
import com.sf.newsapp.model.News;
import com.sf.newsapp.remote.NewsService;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNewsActivity extends AppCompatActivity {

    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListNewsAdapter adapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipeListNews;
    DiagonalLayout diagonalLayout;
    KenBurnsView top_image;
    TextView top_author, top_title;
    String source = "", webHotUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        mService = Common.getNewsService();

        swipeListNews = (SwipeRefreshLayout) findViewById(R.id.swipeListNews);
        swipeListNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout = (DiagonalLayout) findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // click to hot / latest news to read
                Intent intent = new Intent(getBaseContext(), NewsDetailActivity.class);
                intent.putExtra("webUrl", webHotUrl);
                startActivity(intent);
            }
        });
        top_image = (KenBurnsView) findViewById(R.id.top_image);
        top_author = (TextView) findViewById(R.id.top_author);
        top_title = (TextView) findViewById(R.id.top_title);

        dialog = new SpotsDialog(this);

        lstNews = (RecyclerView) findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");

            if (!source.isEmpty()) {
                loadNews(source, false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            dialog.show();
            mService.getArticles(source).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    dialog.dismiss();
                    Picasso.with(getBaseContext())
                            .load(response.body().getArticles().get(0).getUrlToImage())
                            .into(top_image);

                    top_title.setText(response.body().getArticles().get(0).getTitle());
                    top_author.setText(response.body().getArticles().get(0).getAuthor());
                    webHotUrl = response.body().getArticles().get(0).getUrl();

                    List<Article> removeFirstItem = response.body().getArticles();
                    removeFirstItem.remove(0);
                    adapter = new ListNewsAdapter(getBaseContext(), removeFirstItem);
                    adapter.notifyDataSetChanged();
                    lstNews.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
        } else {
            dialog.show();
            mService.getArticles(source).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    dialog.dismiss();
                    Picasso.with(getBaseContext())
                            .load(response.body().getArticles().get(0).getUrlToImage())
                            .into(top_image);

                    top_title.setText(response.body().getArticles().get(0).getTitle());
                    top_author.setText(response.body().getArticles().get(0).getAuthor());
                    webHotUrl = response.body().getArticles().get(0).getUrl();

                    List<Article> removeFirstItem = response.body().getArticles();
                    removeFirstItem.remove(0);
                    adapter = new ListNewsAdapter(getBaseContext(), removeFirstItem);
                    adapter.notifyDataSetChanged();
                    lstNews.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
            swipeListNews.setRefreshing(false);
        }
    }
}
