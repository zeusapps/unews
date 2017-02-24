package ua.in.zeusapps.ukrainenews.services;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Article;

public interface IArticleService {
    @GET("api/article/{sourceId}")
    Observable<List<Article>> getArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count);

    @GET("api/article/{sourceId}")
    Observable<List<Article>> getNewerArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count,
            @Query("published") String published,
            @Query("isAfter") boolean isAfter);
}