package edu.byuh.cis.cs300.ns;

import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Timer class triggers regular tick updates
 */
public class Timer extends Handler implements TickListener {

    private List<TickListener> observers;

    /**
     * Constructor initializes observer list and starts ticking
     */
    public Timer() {
        observers = new CopyOnWriteArrayList<>();  // Thread-safe list
        sendMessageDelayed(obtainMessage(), 0);
    }

    /**
     * Tick implementation for Timer
     */
    @Override
    public void tick() {}

    /**
     * Registers new listener
     * @param o
     */
    @Override
    public void registerListener(TickListener o) {
        observers.add(o);
    }

    /**
     * Removes a listener
     * @param o
     */
    @Override
    public void removeListener(TickListener o) {
        observers.remove(o);
    }

    /**
     * Removes all listener
     */
    @Override
    public void removeAllListener() {
        observers.clear();
    }

    /**
     * It notifies all registered listeners, then schedules ticks
     * @param m
     */
    @Override
    public void handleMessage(Message m) {
        for (var o : observers) {
            o.tick();
        }
        sendMessageDelayed(obtainMessage(), 10);
    }
}
