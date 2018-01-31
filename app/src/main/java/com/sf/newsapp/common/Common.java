package com.sf.newsapp.common;

import com.sf.newsapp.remote.NewsService;
import com.sf.newsapp.remote.RetrofitClient;

/**
 * Created by mesutgenc on 31.01.2018.
 */

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";

    public static final String API_KEY = "254a6bf8848c4a7a85d4d2762f2de433";

    public static NewsService getNewsService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }
}
