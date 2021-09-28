package com.teamapricot.projectwalking.observe;

import java.util.HashSet;
import java.util.Set;

public abstract class ObservableBase<T> implements Observable<T> {
    private Set<Observer<T>> observers = new HashSet<>();

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void updateObservers(T observable) {
        for (Observer<T> observer : observers) {
            observer.update(observable);
        }
    }
}
