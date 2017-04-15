package ua.in.zeusapps.ukrainenews.common;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class Interactor<ResultType, ParameterType> {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
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

    public void execute(ParameterType parameter, Subscriber<ResultType> subscriber) {
        Subscription subscription = buildObservable(parameter)
                .subscribeOn(jobScheduler)
                .observeOn(uiScheduler)
                .subscribe(subscriber);

        compositeSubscription.add(subscription);
    }

    public void execute(Subscriber<ResultType> subscriber) {
        execute(null, subscriber);
    }

    public void unsubscribe() {
        compositeSubscription.clear();
    }
}