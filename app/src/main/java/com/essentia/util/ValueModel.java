package com.essentia.util;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public class ValueModel<T> {

    private T value;
    final private ArrayList<ChangeListener<T>> listeners =
            new ArrayList<ChangeListener<T>>();

    public interface ChangeListener<T> {
        void onValueChanged(ValueModel<T> instance, T oldValue, T newValue);
    };

    public ValueModel() {
        value = null;
    }

    public ValueModel(T value) {
        this.value = value;
    }

    public void set(T newValue) {
        if (value == null && newValue == null) {
            return;
        } else if (value != null && newValue != null) {
            if (value.equals(newValue))
                return;
        }

        T oldValue = value;
        value = newValue;

        /**
         * iterate over copy so that this can be modified during iteration
         * (i.e by onValueChanged())
         */
        ArrayList<ChangeListener<T>> copy = new ArrayList<ChangeListener<T>>(listeners);
        for (ChangeListener<T> l : copy) {
            l.onValueChanged(this, oldValue, newValue);
        }
    }

    public T get() {
        return value;
    }

    public void registerChangeListener(ChangeListener<T> listener) {
        listeners.add(listener);
    }

    public void unregisterChangeListener(ChangeListener<T> listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }
}
