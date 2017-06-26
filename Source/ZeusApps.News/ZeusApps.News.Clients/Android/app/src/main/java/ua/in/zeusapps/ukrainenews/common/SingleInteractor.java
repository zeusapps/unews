package ua.in.zeusapps.ukrainenews.common;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public abstract class SingleInteractor<ResultType, ParameterType> {
        private final CompositeDisposable compositeDisposable = new CompositeDisposable();
        protected final Scheduler jobScheduler;
        private final Scheduler uiScheduler;

        public SingleInteractor(Scheduler jobScheduler, Scheduler uiScheduler){
            this.jobScheduler = jobScheduler;
            this.uiScheduler = uiScheduler;
        }

        public SingleInteractor() {
            this.jobScheduler = Schedulers.computation();
            this.uiScheduler = AndroidSchedulers.mainThread();
        }

        protected abstract Single<ResultType> build(ParameterType parameter);

        public void execute(
                Consumer<ResultType> resultConsumer,
                Consumer<? super Throwable> errorConsumer,
                ParameterType parameter){

            Single<ResultType> observable = build(parameter)
                    .subscribeOn(jobScheduler)
                    .observeOn(uiScheduler);


            Disposable disposable = observable.subscribe(resultConsumer, errorConsumer);
            compositeDisposable.add(disposable);
        }

        public void execute(
                Consumer<ResultType> resultConsumer,
                Consumer<? super Throwable> errorConsumer){

            execute(resultConsumer, errorConsumer, null);
        }


        public void execute(
                Consumer<ResultType> resultConsumer,
                ParameterType parameter) {
            execute(resultConsumer, Functions.ERROR_CONSUMER, parameter);
        }

        public void execute(Consumer<ResultType> resultConsumer) {
            execute(resultConsumer, Functions.ERROR_CONSUMER, null);
        }

        public void unsubscribe() {
            compositeDisposable.clear();
        }
    }
