package de.salzfrei.objects;

import java.util.concurrent.ConcurrentHashMap;

public class TabMemory {

    public static final ConcurrentHashMap<Object, TabMemory> MEMORY = new ConcurrentHashMap<>(1024);

    public final int transactionId;
    public final String request;

    public TabMemory(int transactionId, String request) {
        this.transactionId = transactionId;
        this.request = request;
    }

    @Override
    public String toString() {
        return "TabMemory{" +
                "transactionId=" + transactionId +
                ", request='" + request + '\'' +
                '}';
    }

}
