package ua.in.zeusapps.ukrainenews.common;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class Interactor<ResultType, ParameterType> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
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

    public void executeWithError(ParameterType parameter,
                        Consumer<ResultType> resultConsumer,
                        Consumer<? super Throwable> errorConsumer){
        Observable<ResultType> observable = buildObservable(parameter)
                .subscribeOn(jobScheduler)
                .observeOn(uiScheduler);

        if (errorConsumer != null){
            observable = observable.doOnError(errorConsumer);
        }

        Disposable disposable = observable.subscribe(resultConsumer);
        compositeDisposable.add(disposable);
    }

    public void executeWithError(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer){
        executeWithError(null, resultConsumer, errorConsumer);
    }


    public void execute(ParameterType parameter, Consumer<ResultType> consumer) {
        executeWithError(parameter, consumer, null);
    }

    public void execute(Consumer<ResultType> consumer) {
        execute(null, consumer);
    }

    public void unsubscribe() {
        compositeDisposable.clear();
    }
}