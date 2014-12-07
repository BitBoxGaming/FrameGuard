package com.gmail.bitboxgaming.frameguard;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    //Rotation Protection
    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent playerInteractEntityEvent) {
        Player player = playerInteractEntityEvent.getPlayer();
        EntityType entityType = playerInteractEntityEvent.getRightClicked().getType();
        if (player instanceof Player && entityType == EntityType.ITEM_FRAME) {
            if (player.hasPermission("frameguard.canrotate")) {
                return;
            } else {
                playerInteractEntityEvent.setCancelled(true);
                if (getConfig().getBoolean("Show-Fail-Messages")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Rotation-Fail-Message")));
                } else {
                    return;
                }
            }
        }
    }

    //Item Theft Protection
    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        EntityType entityType = event.getEntity().getType();
        if (entity instanceof Player) {
            Player damagerPlayer = ((Player) entity).getPlayer();
            if (entityType.equals(EntityType.ITEM_FRAME)) {
                if (damagerPlayer.hasPermission("frameguard.cansteal")) {
                    return;
                } else {
                    event.setCancelled(true);
                    if (getConfig().getBoolean("Show-Fail-Messages")) {
                        damagerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Theft-Fail-Message")));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    //Frame Break Protection
    @EventHandler
    public void hangingBreakByEntity(HangingBreakByEntityEvent event) {
        Hanging hanging = event.getEntity();
        Entity entity = event.getRemover();
        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            if (hanging instanceof ItemFrame) {
                if (player.hasPermission("frameguard.canbreak")) {
                    return;
                } else {
                    event.setCancelled(true);
                    if (getConfig().getBoolean("Show-Fail-Messages")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Break-Fail-Message")));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fgreload") && sender.hasPermission("frameguard.reload")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&4Frame Guard&6] &ahas been successfully reloaded!"));
            reloadConfig();
        }
        return true;
    }
}
