package com.ehhthan.droplisteners.api.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public interface DropListener<T extends Event> extends Listener {
    @EventHandler
    void onDropEvent(T event);
}
