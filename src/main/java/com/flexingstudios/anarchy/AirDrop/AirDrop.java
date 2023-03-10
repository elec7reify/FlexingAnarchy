package com.flexingstudios.anarchy.AirDrop;

import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.anarchy.AirDrop.loot.LootGenerator;
import com.flexingstudios.anarchy.AirDrop.protection.AirDropListener;
import com.flexingstudios.anarchy.Anarchy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class AirDrop {
    private static Location airDrop;
    private LootGenerator generator;
    private boolean open = false;
    private int task = 0;

    public AirDrop(Location location, LootGenerator generator) {
        airDrop = location;
        this.generator = generator;
        Bukkit.getPluginManager().registerEvents(new AirDropListener(), Anarchy.getInstance());
    }

    public void openChest() {
        airDrop.getChunk().load();
        AirDropUtil.randomFillChest(airDrop, generator.loot());

        open = true;
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), this::closeChest, 600L);
        Utilities.bcast("&cAirDrop спустился! Координаты: X:" + airDrop.getX() + " Y:" + airDrop.getY() + " Z:" + airDrop.getZ());
    }

    public void closeChest() {
        if (airDrop.getBlock().getState() instanceof Chest) {
            airDrop.getBlock().setType(Material.AIR);
        }
        open = false;
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), this::openChest, 1200L);
        // Bukkit.getWorld("world").createExplosion(airDrop, 2.0F, false);
        Utilities.bcast("&cAirDrop разрушился.");
    }

    public void start() {
        airDrop.getBlock().setType(Material.CHEST);
        open = false;
        Utilities.bcast(" &7На следующих координатах появился Аирдроп. X:" + airDrop.getX() + " Y:" + airDrop.getY() + " Z:" + airDrop.getZ());
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), this::openChest, 1200L);
    }

    public void stop() {
        open = false;
        Bukkit.getScheduler().cancelTask(task);
        task = -1;
    }

    public LootGenerator getGenerator() {
        return generator;
    }

    public Location getChestLocation() {
        return airDrop;
    }

    public boolean isOpen() {
        return open;
    }
}
