package com.flexingstudios.anarchy.AirDrop.protection;

import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.anarchy.Anarchy;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AirDropListener implements Listener {
    private int size = 5 * 10;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.getGameMode() != GameMode.CREATIVE && isAirDrop(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.getGameMode() != GameMode.CREATIVE && isAirDrop(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysic(BlockPhysicsEvent event) {
        Block block = event.getBlock();

        if (isAirDrop(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!Anarchy.airDrop.isOpen() && Anarchy.airDrop.getChestLocation() == event.getClickedBlock().getLocation()) {
            Utilities.msg(player, "&cПодождите немного перед тем, как открыть AirDrop!");
        }
    }

    private Location

    private boolean isAirDrop(Block block) {
        return block.getLocation().distanceSquared(Anarchy.airDrop.getChestLocation()) < size;
    }
}
