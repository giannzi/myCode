package edu.byuh.cis.cs300.ns;

/**
 * Interface for objects that need to receive
 * "tick" updates from timer
 */
public interface TickListener {
    void tick();
    void registerListener(TickListener o);
    void removeListener(TickListener o);
    void removeAllListener();
}
