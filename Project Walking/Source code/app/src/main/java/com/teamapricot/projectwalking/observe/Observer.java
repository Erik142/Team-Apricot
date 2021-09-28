package com.teamapricot.projectwalking.observe;

public interface Observer<T> {
    void update(T observable);
}
