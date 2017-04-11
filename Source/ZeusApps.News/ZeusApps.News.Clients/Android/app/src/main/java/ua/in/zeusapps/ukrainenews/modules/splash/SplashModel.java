package ua.in.zeusapps.ukrainenews.modules.splash;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.common.BaseModel;
import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IRepository;
import ua.in.zeusapps.ukrainenews.services.IDataService;

public class SplashModel
        extends BaseModel
        implements SplashMVP.IModel {

    private final IRepository _repository;
    private final IDataService _dataService;

    public SplashModel(
            IRepository repository,
            IDataService dataService){
        _repository = repository;
        _dataService = dataService;
    }

    public Observable<List<Source>> ensureSources(){
        final List<Source> tempSources = _repository.getAllSources();

        Observable<List<Source>> observable = _dataService
                .getSources()
                .map(new Func1<List<Source>, List<Source>>() {
                    @Override
                    public List<Source> call(List<Source> sources) {
                        List<Source> deleteSources = subtract(tempSources, sources);
                        List<Source> addSources = subtract(sources, tempSources);
                        _repository.deleteAllSources(deleteSources);
                        _repository.addAllSources(addSources);
                        return sources;
                    }
                });

        if (tempSources.size() > 0){
            observable = observable.onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Source>>>() {
                @Override
                public Observable<? extends List<Source>> call(Throwable throwable) {
                    return Observable.just(tempSources);
                }
            });
        }

        return wrapObservable(observable);
    }

    private List<Source> subtract(List<Source> first, List<Source> second){
        List<Source> result = new ArrayList<>();

        for (Source firstSource: first) {
            boolean found = false;
            for (Source secondSource: second){
                if (firstSource.equals(secondSource)){
                    found = true;
                    break;
                }
            }
            if (!found){
                result.add(firstSource);
            }
        }

        return result;
    }
}
