package am.management.management_api.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    public static volatile AtomicInteger successfullySavedComment = new AtomicInteger(0);
    public static volatile AtomicInteger successfullyDeliveredComment = new AtomicInteger(0);
}
