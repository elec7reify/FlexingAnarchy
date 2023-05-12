package com.flexingstudios.flexinganarchy.AirDrop;

import com.flexingstudios.FlexingNetwork.api.geom.Vec3f;
import com.flexingstudios.FlexingNetwork.api.holo.Hologram;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.flexinganarchy.AirDrop.loot.LootGenerator;
import com.flexingstudios.flexinganarchy.AirDrop.loot.generators.StandardLootGenerator;
import com.flexingstudios.flexinganarchy.FlexingAnarchy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import java.util.logging.Level;

public class AirDrop {
    private static AirDrop instance;
    public final Location airDrop;
    private final LootGenerator generator;
    public static Chest chest;
    private boolean open = false;
    private int task = 0;
    public static Hologram hologram;
    public static Location holoLocation;

    public AirDrop(Location location, LootGenerator generator) {
        airDrop = location;
        this.generator = generator;
        cooldown();
    }

    private void spawnChest() {
        airDrop.getChunk().load();
        AirDropUtil.fallingChest(airDrop);
        airDrop.getBlock().setType(Material.CHEST);
        AirDropUtil.randomFillChest(airDrop, generator.loot());

        chest = (Chest) airDrop.getBlock().getState();
        hologram = Hologram.create(holoLocation, "&cАирДроп");
        open = true;
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingAnarchy.Companion.getInstance(), this::breakChest, 600L);
        Utilities.Companion.broadcast("&7AirDrop спустился! Координаты: X:" + airDrop.getX() + " Y:" + airDrop.getY() + " Z:" + airDrop.getZ());
    }

    private void breakChest() {
        if (airDrop.getBlock().getState() instanceof Chest) {
            chest.getBlockInventory().clear();
            airDrop.getBlock().setType(Material.AIR);
        }
        hologram.clear();
        open = false;
        task = -1;
        // Bukkit.getWorld("world").createExplosion(airDrop, 2.0F, false);
    }

    public void cooldown() {
        Vec3f loc = new Vec3f(airDrop).add(0.5F, 0.9F, 0.5F);
        holoLocation = new Location(airDrop.getWorld(), loc.x, loc.y, loc.z);
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingAnarchy.Companion.getInstance(), this::spawnChest, 40L);
    }

    public void stop() {
        open = false;
        Bukkit.getScheduler().cancelTask(task);
        task = -1;
        breakChest();
    }

    public static void start() {
        instance = new AirDrop(AirDropUtil.generateLocation(), new StandardLootGenerator());
        FlexingAnarchy.Companion.getInstance().getLogger().log(Level.INFO, AirDrop.getInstance().airDrop.toString());
    }

    public static AirDrop getInstance() {
        return instance;
    }

    public Hologram getHologram() {
        return hologram;
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
