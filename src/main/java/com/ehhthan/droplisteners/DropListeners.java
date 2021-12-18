package com.ehhthan.droplisteners;

import com.ehhthan.droplisteners.api.listeners.type.FishingListener;
import com.ehhthan.droplisteners.command.DropListenersCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropListeners extends JavaPlugin {
    private FishingListener fishingListener;

    private static DropListeners INSTANCE;

    public static DropListeners getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();

        this.fishingListener = new FishingListener(getConfig().getConfigurationSection("listeners.fishing"));
        getServer().getPluginManager().registerEvents(fishingListener, this);
        getCommand("droplisteners").setExecutor(new DropListenersCommand());
    }

    public void reload() {
        reloadConfig();
        fishingListener.reload(getConfig().getConfigurationSection("listeners.fishing"));
    }
}
