package ua.in.zeusapps.ukrainenews.modules.sources;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Source;
import ua.in.zeusapps.ukrainenews.services.IRepository;

public class SourcesModel {

    private IRepository _repository;

    public SourcesModel(IRepository repository) {
        _repository = repository;
    }

    public List<Source> getSources(){
        return _repository.getAllSources();
    }
}
