package com.teamapricot.projectwalking.observe;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class used to implement the base functionality for Observables in the Observer-Observable pattern
 * @param <T> The type of Observable to use
 */
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
