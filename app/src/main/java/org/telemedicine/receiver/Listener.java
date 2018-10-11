package org.telemedicine.receiver;

public interface Listener<T> {
    void onEvent(T data);
}