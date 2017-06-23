package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.common.Interactor;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IDataService;

public class GetSourcesInteractor extends Interactor<List<Source>, Void> {

    private IDataService _dataService;

    @Inject
    public GetSourcesInteractor(IDataService dataService) {
        _dataService = dataService;
    }

    @Override
    protected Observable<List<Source>> buildObservable(Void parameter) {
        return _dataService.getSources();
    }
}
