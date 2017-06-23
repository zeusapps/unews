package ua.in.zeusapps.ukrainenews.common;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class Interactor<ResultType, ParameterType> {

    private final CompositeDisposable disposable = new CompositeDisposable();
    protected final Scheduler jobScheduler;
    private final Scheduler uiScheduler;

    public Interactor(Scheduler jobScheduler, Scheduler uiScheduler){
        this.jobScheduler = jobScheduler;
        this.uiScheduler = uiScheduler;
    }

    public Interactor() {
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    protected abstract Observable<ResultType> buildObservable(ParameterType parameter);

    public void execute(ParameterType parameter, Consumer<ResultType> consumer) {
        Disposable subscription = buildObservable(parameter)
                .subscribeOn(jobScheduler)
                .observeOn(uiScheduler)
                .subscribe(consumer);

        disposable.add(subscription);
    }

    public void execute(Consumer<ResultType> consumer) {
        execute(null, consumer);
    }

    public void unsubscribe() {
        disposable.clear();
    }
}