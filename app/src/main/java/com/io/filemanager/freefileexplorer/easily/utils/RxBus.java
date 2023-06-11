package com.io.filemanager.freefileexplorer.easily.utils;

import java.util.HashMap;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subscriptions.CompositeSubscription;

public class RxBus {
    private static volatile RxBus mInstance;
    private SerializedSubject<Object, Object> mSubject = new SerializedSubject<>(PublishSubject.create());
    private HashMap<String, CompositeSubscription> mSubscriptionMap;

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void post(Object obj) {
        this.mSubject.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> cls) {
        return this.mSubject.ofType(cls);
    }

    public boolean hasObservers() {
        return this.mSubject.hasObservers();
    }

    public <T> Subscription doSubscribe(Class<T> cls, Action1<T> action1, Action1<Throwable> action12) {
        return toObservable(cls).subscribeOn(Schedulers.io()).subscribe(action1, action12);
    }

    public void addSubscription(Object obj, Subscription subscription) {
        if (this.mSubscriptionMap == null) {
            this.mSubscriptionMap = new HashMap<>();
        }
        String name = obj.getClass().getName();
        if (this.mSubscriptionMap.get(name) != null) {
            this.mSubscriptionMap.get(name).add(subscription);
            return;
        }
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(subscription);
        this.mSubscriptionMap.put(name, compositeSubscription);
    }

    public void unSubscribe(Object obj) {
        if (this.mSubscriptionMap != null) {
            String name = obj.getClass().getName();
            if (this.mSubscriptionMap.containsKey(name)) {
                if (this.mSubscriptionMap.get(name) != null) {
                    this.mSubscriptionMap.get(name).unsubscribe();
                }
                this.mSubscriptionMap.remove(name);
            }
        }
    }
}
