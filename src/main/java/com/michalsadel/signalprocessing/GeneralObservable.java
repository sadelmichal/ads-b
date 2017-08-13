package com.michalsadel.signalprocessing;

import java.util.*;

class GeneralObservable<T> {
    private final Set<T> observers = new HashSet<>();

    Set<T> getObservers() {
        return observers;
    }

    public void attach(T observer) {
        Optional.ofNullable(observer).ifPresent(x -> observers.add(x));
    }
}
