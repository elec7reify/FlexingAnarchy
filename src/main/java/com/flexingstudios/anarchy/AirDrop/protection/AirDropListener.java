package com.flexingstudios.anarchy.AirDrop.protection;

import com.flexingstudios.FlexingNetwork.api.holo.Hologram;
import com.flexingstudios.anarchy.AirDrop.AirDrop;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AirDropListener implements Listener {
    private int size = 5 * 10;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.CHEST
                && AirDrop.getInstance().getChestLocation().equals(block.getLocation())
                && AirDrop.getInstance().getHologram() != null)
            AirDrop.getInstance().getHologram().clear();
        if (player.getGameMode() != GameMode.CREATIVE && isAirDrop(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getLocation() == AirDrop.getInstance().getChestLocation()) Hologram.create(AirDrop.holoLocation, "&cАирДроп");
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

    private boolean isAirDrop(Block block) {
        return block.getLocation().distanceSquared(AirDrop.getInstance().getChestLocation()) < size;
    }
}
