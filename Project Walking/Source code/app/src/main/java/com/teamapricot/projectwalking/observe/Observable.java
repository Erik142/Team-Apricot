package com.teamapricot.projectwalking.observe;

/**
 * Interface for the "Observable" part of the Observer-Observable pattern
 * @param <T> The type of Observable to use
 */
public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void updateObservers(T observable);
}
