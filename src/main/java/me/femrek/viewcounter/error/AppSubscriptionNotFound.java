package me.femrek.viewcounter.error;

public class AppSubscriptionNotFound extends RuntimeException {
    public AppSubscriptionNotFound(String message) {
        super(message);
    }
}
