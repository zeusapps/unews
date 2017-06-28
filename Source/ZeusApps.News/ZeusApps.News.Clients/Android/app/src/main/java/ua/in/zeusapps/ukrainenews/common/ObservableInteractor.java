package ua.in.zeusapps.ukrainenews.common;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public abstract class ObservableInteractor<ResultType, ParameterType> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected final Scheduler jobScheduler;
    private final Scheduler uiScheduler;

    public ObservableInteractor(Scheduler jobScheduler, Scheduler uiScheduler){
        this.jobScheduler = jobScheduler;
        this.uiScheduler = uiScheduler;
    }

    public ObservableInteractor() {
        this.jobScheduler = Schedulers.computation();
        this.uiScheduler = AndroidSchedulers.mainThread();
    }

    protected abstract Observable<ResultType> build(ParameterType parameter);

    private Observable<ResultType> buildIO(ParameterType parameter){
        return build(parameter).subscribeOn(jobScheduler).observeOn(uiScheduler);
    }

    public void register(Disposable disposable){
        compositeDisposable.add(disposable);
    }

    public void execute(Observer<? super ResultType> observer, ParameterType parameter){
        Observable<ResultType> observable = buildIO(parameter);
        observable.subscribe(observer);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer,
            Action onComplete,
            Consumer<? super Disposable> subscribeConsumer,
            ParameterType parameter){

        Observable<ResultType> observable = buildIO(parameter);
        Disposable disposable = observable.subscribe(
                resultConsumer, errorConsumer, onComplete, subscribeConsumer);
        compositeDisposable.add(disposable);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer,
            Action onComplete,
            Consumer<? super Disposable> subscribeConsumer){

        execute(resultConsumer, errorConsumer, onComplete, subscribeConsumer, null);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer,
            Action onComplete,
            ParameterType parameter) {
        execute(resultConsumer, errorConsumer, onComplete, Functions.emptyConsumer(), parameter);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer,
            Action onComplete) {
        execute(resultConsumer, errorConsumer, onComplete, Functions.emptyConsumer(), null);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer,
            ParameterType parameter) {
        execute(resultConsumer, errorConsumer, Functions.EMPTY_ACTION, Functions.emptyConsumer(), parameter);
    }

    public void execute(
            Consumer<ResultType> resultConsumer,
            Consumer<? super Throwable> errorConsumer) {
        execute(resultConsumer, errorConsumer, Functions.EMPTY_ACTION, Functions.emptyConsumer(), null);
    }


    public void execute(
            Consumer<ResultType> resultConsumer,
            ParameterType parameter) {
        execute(resultConsumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer(), parameter);
    }

    public void execute(Consumer<ResultType> resultConsumer) {
        execute(resultConsumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer(), null);
    }



    public void unsubscribe() {
        compositeDisposable.clear();
    }
}