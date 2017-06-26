package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.models.Source;


class SourceRepository
        extends RepositoryBase<Source, String>
        implements ISourceRepository {

    SourceRepository(Context context) {
        super(context, Source.class);
    }

    @Override
    public Single<List<Source>> getAll() {
        return Single.fromCallable(() -> getDao().queryForAll());
    }

    @Override
    public Single<Source> getById(String id) {
        return Single.fromCallable(() -> getDao().queryForId(id));
    }

    @Override
    public Single<List<Source>> checkSources(List<Source> remoteSources) {
        return Single.fromCallable(() -> {
            List<Source> localSources = getDao().queryForAll();

            for (Source source : localSources) {
                if (!remoteSources.contains(source)) {
                    delete(source);
                }
            }

            for (Source source : remoteSources) {
                if (!localSources.contains(source)) {
                    create(source);
                }
            }

            return getDao().queryForAll();
        });
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

    @Override
    public void updateTimestamp(Source source) {
        source.setTimestamp(new Date());
        update(source);
    }
}
