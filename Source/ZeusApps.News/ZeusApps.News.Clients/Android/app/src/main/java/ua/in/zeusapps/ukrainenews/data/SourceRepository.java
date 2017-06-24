package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;



public class SourceRepository
        extends RepositoryBase<Source, String>
        implements ISourceRepository {

    public SourceRepository(Context context) {
        super(context, Source.class);
    }

    @Override
    public List<Source> getAll() {
        return getDao().queryForAll();
    }

    @Override
    public Source getById(String id) {
        return getDao().queryForId(id);
    }

    @Override
    public void create(Source source) {
        getDao().create(source);
    }

    @Override
    public void delete(Source source) {
        getDao().delete(source);
    }

    @Override
    public void update(Source source) {
        getDao().update(source);
    }
}
