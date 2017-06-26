package ua.in.zeusapps.ukrainenews.domain;

import javax.inject.Inject;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.common.SingleInteractor;
import ua.in.zeusapps.ukrainenews.data.ISourceRepository;
import ua.in.zeusapps.ukrainenews.models.Source;

public class GetSourceInteractor
    extends SingleInteractor<Source, String> {

    private final ISourceRepository _sourceRepository;

    @Inject
    public GetSourceInteractor(ISourceRepository sourceRepository) {
        _sourceRepository = sourceRepository;
    }

    @Override
    protected Single<Source> build(String id) {
        return _sourceRepository.getById(id);
    }
}
