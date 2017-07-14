package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IDataService {
    @GET("api/sources/{sourceId}/articles")
    Single<List<Article>> getArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count);

    @GET("api/sources/{sourceId}/articles")
    Single<List<Article>> getArticles(
            @Path("sourceId") String sourceId,
            @Query("count") int count,
            @Query("timestamp") long timestamp,
            @Query("isAfter") boolean isAfter);

    @GET("api/sources")
    Single<List<Source>> getSources();
}