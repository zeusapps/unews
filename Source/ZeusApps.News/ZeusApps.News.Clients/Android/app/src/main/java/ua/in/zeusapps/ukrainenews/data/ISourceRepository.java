package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;

public interface ISourceRepository {
    List<Source> getAll();
    void create(Source source);
    void delete(Source source);
    void update(Source source);
}
