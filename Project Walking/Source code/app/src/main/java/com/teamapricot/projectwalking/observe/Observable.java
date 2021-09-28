package com.teamapricot.projectwalking.observe;

public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void updateObservers(T observable);
}
