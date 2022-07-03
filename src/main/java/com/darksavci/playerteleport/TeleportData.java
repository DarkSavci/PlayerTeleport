package com.darksavci.playerteleport;

import java.util.UUID;

public class TeleportData {

    public int block;
    public UUID player;
    public UUID teleportPlayer;

    public TeleportData(int block, UUID player, UUID teleportPlayer) {
        this.block = block;
        this.player = player;
        this.teleportPlayer = teleportPlayer;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public UUID getTeleportPlayer() {
        return teleportPlayer;
    }

    public void setTeleportPlayer(UUID teleportPlayer) {
        this.teleportPlayer = teleportPlayer;
    }
}
