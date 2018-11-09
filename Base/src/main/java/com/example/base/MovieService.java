package com.example.base;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    //获取豆瓣Top250 榜单

    /**
     * BASE_URL = "https://api.douban.com/v2/movie/";
     * FINAL_URL = "https://api.douban.com/v2/movie/start=0&count=20";
     */
    @GET("top250")
    Observable<MovieSubject> getTop250(@Query("start") int start, @Query("count") int count);
}
