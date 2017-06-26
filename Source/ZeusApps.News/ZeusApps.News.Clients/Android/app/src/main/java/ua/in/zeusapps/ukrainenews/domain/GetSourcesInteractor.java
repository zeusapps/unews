package ua.in.zeusapps.ukrainenews.domain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.IDataService;
import ua.in.zeusapps.ukrainenews.models.Source;

class GetSourcesInteractor extends SingleInteractor<List<Source>, Void> {

    private IDataService _dataService;

    @Inject
    GetSourcesInteractor(IDataService dataService) {
        _dataService = dataService;
    }

    @Override
    protected Single<List<Source>> build(Void parameter) {
        return _dataService.getSources();
    }
}
