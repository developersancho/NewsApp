package com.sf.newsapp.remote;

import com.sf.newsapp.model.Article;
import com.sf.newsapp.model.News;
import com.sf.newsapp.model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mesutgenc on 31.01.2018.
 */

public interface NewsService {
    @GET("v2/sources?language=en&apiKey=254a6bf8848c4a7a85d4d2762f2de433")
    Call<WebSite> getSources();

    @GET("v2/top-headlines?apiKey=254a6bf8848c4a7a85d4d2762f2de433")
    Call<News> getArticles(@Query("sources") String sources);
}
