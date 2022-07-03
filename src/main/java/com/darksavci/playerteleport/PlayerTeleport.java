package com.darksavci.playerteleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class PlayerTeleport extends JavaPlugin implements Listener, CommandExecutor {
    public HashMap<UUID, TeleportData> teleports = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("tpcancel")).setExecutor(this);
        Objects.requireNonNull(getCommand("tpplayer")).setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("tpcancel")) {
            if (args.length < 1) {
                sender.sendMessage("Usage: /tpcancel <player>");
                return false;
            }
            String playerName = args[0];
            if (Bukkit.getPlayer(playerName) == null) {
                sender.sendMessage("Oyunucu bulunamadı");
                return true;
            }

            Player player = Bukkit.getPlayer(playerName);
            if (teleports.containsKey(player.getUniqueId())) {
                teleports.remove(player.getUniqueId());
            }
        }

        if (command.getName().equalsIgnoreCase("tpplayer")) {

            if (!(sender instanceof Player)) return false;

            if (args.length < 2) {
                sender.sendMessage("Usage: /pt <player> <block>");
                return true;
            }

            String playerName = args[0];
            String blockStr = args[1];

            if (Bukkit.getPlayer(playerName) == null) {
                sender.sendMessage("Oyunucu bulunamadı");
                return true;
            }

            if (Integer.parseInt(blockStr) <= 0) {
                sender.sendMessage("Geçerli bir sayı giriniz");
                return true;
            }

            Player player = Bukkit.getPlayer(playerName);
            int block = Integer.parseInt(blockStr);
            Player senderPlayer = (Player) sender;

            if (teleports.containsKey(player.getUniqueId())) {
                teleports.remove(player.getUniqueId());
            }

            teleports.put(senderPlayer.getUniqueId(), new TeleportData(block, senderPlayer.getUniqueId(), player.getUniqueId()));

            return true;
        }
        return false;
    }

    public Location faceDirection(Location location, Location target) {
        Vector dir = target.clone().subtract(location).toVector();
        Location loc = location.setDirection(dir);
        return loc;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player walker = event.getPlayer();

        if(!teleports.containsKey(walker.getUniqueId())) return;

        TeleportData data = teleports.get(walker.getUniqueId());

        Player teleporter = Bukkit.getPlayer(data.getTeleportPlayer());
        int block = data.getBlock();

        Location eyeLoc = walker.getEyeLocation();
        Location tpLoc = faceDirection(eyeLoc.clone().add(0, 0, block), eyeLoc);
        tpLoc.setY(tpLoc.getY() - 1);
        Location playerLoc = teleporter.getLocation().clone();

        if(playerLoc.distance(tpLoc) > 50) {
            teleporter.teleport(tpLoc);
        }

        if (tpLoc.getBlockX() != playerLoc.getBlockX() || tpLoc.getBlockY() != playerLoc.getBlockY()) {
            teleporter.setVelocity(tpLoc.subtract(playerLoc).toVector().multiply(0.15));
        }
    }
}
