package ua.in.zeusapps.ukrainenews.modules.source;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.ISourceService;

public class SourceModel extends BaseModel implements SourceMVP.IModel {
    private final ISourceService _service;

    public SourceModel() {
        _service = getRetrofit().create(ISourceService.class);
    }

    @Override
    public Observable<List<Source>> getSources() {
        return wrapObservable(_service.getSources());
    }
}