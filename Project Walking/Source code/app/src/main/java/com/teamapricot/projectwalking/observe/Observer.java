package com.teamapricot.projectwalking.observe;

/**
 * Interface for the "Observer" part of the Observer-Observable pattern
 * @param <T> The type of Observable to use
 */
public interface Observer<T> {
    void update(T observable);
}
