package org.acornmc.drsleep;

import org.acornmc.drsleep.configuration.ConfigManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.UUID;
import java.util.Set;
import org.bukkit.scheduler.BukkitScheduler;

public final class DrSleep extends JavaPlugin {

    public static DrSleep plugin;
    public static Set<UUID> nosleep = new HashSet<>();
    public static Set<UUID> phantom = new HashSet<>();

    ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager(this);

        getCommand("nosleep").setExecutor(new CommandNoSleep(configManager));
        getCommand("drsleep").setExecutor(new CommandDrSleep(configManager));
        getCommand("phantom").setExecutor(new CommandPhantom(configManager));

        getServer().getPluginManager().registerEvents(new EventPlayerBedEnter(configManager), this);
        getServer().getPluginManager().registerEvents(new EventLogout(), this);
        getServer().getPluginManager().registerEvents(new EventPlayerWorldSwitch(configManager), this);

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            World world = Bukkit.getWorld(configManager.get().getString("World"));
            if (world == null) {
                System.err.println("[DrSleep] Invalid world found in config.");
                return;
            }
            if (world.getTime() < 20L) {
                if (configManager.get().getBoolean("ClearNoSleepDaily")) {
                    nosleep.clear();
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
