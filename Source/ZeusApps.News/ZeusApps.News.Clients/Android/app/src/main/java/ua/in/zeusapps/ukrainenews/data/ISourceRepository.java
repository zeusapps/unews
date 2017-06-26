package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface ISourceRepository {
    Single<List<Source>> getAll();
    Single<Source> getById(String id);
    Single<List<Source>> checkSources(List<Source> remoteSources);
    void create(Source source);
    void delete(Source source);
    void update(Source source);
    void updateTimestamp(Source source);
}
