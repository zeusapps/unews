package ua.in.zeusapps.ukrainenews.services;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ISourceService {

    @GET("api/source")
    Observable<List<Source>> getSources();

}
