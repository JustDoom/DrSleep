package org.acornmc.drsleep;

import org.acornmc.drsleep.configuration.ConfigManager;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class CommandPhantom implements CommandExecutor {
    Set<UUID> phantom;
    ConfigManager configManager;

    public CommandPhantom(ConfigManager configManager) {
        this.phantom = DrSleep.phantom;
        this.configManager = configManager;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only");
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("drsleep.phantom")) {
            player.sendMessage(configManager.get().getString("NoPerms").replace("&", "§"));
            return true;
        }
        World playerWorld = player.getWorld();
        if (!playerWorld.getName().equals(configManager.get().getString("World"))) {
            player.sendMessage(configManager.get().getString("NotInConfiguredWorld").replace("&", "§"));
            return true;
        }
        if (phantom.contains(player.getUniqueId())) {
            phantom.remove(player.getUniqueId());
            player.sendMessage(configManager.get().getString("RemovedFromNoSleep").replace("&", "§"));
            return true;
        }
        phantom.add(player.getUniqueId());
        player.sendMessage(configManager.get().getString("AddedToNoSleep").replace("&", "§"));
        return true;
    }
}