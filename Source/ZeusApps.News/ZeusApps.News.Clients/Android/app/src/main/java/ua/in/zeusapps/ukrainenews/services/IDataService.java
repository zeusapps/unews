package ua.in.zeusapps.ukrainenews.services;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IDataService {
    @GET("api/sources/{sourceId}/articles")
    Observable<List<Article>> getArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count);

    @GET("api/sources/{sourceId}/articles")
    Observable<List<Article>> getNewerArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count,
            @Query("published") String published,
            @Query("isAfter") boolean isAfter);

    @GET("api/sources")
    Observable<List<Source>> getSources();
}