package com.flexingstudios.anarchy.AirDrop;

import com.flexingstudios.FlexingNetwork.api.geom.Vec3f;
import com.flexingstudios.FlexingNetwork.api.holo.Hologram;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.anarchy.AirDrop.loot.LootGenerator;
import com.flexingstudios.anarchy.AirDrop.loot.generators.StandardLootGenerator;
import com.flexingstudios.anarchy.AirDrop.protection.AirDropListener;
import com.flexingstudios.anarchy.Anarchy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class AirDrop {
    private static AirDrop instance;
    public final Location airDrop;
    private final LootGenerator generator;
    private boolean open = false;
    private int task = 0;
    public static Hologram hologram;
    public static Location holoLocation;

    public AirDrop(Location location, LootGenerator generator) {
        airDrop = location;
        this.generator = generator;
        Bukkit.getPluginManager().registerEvents(new AirDropListener(), Anarchy.getInstance());
        cooldown();
    }

    private void openChest() {
        airDrop.getChunk().load();
        airDrop.getBlock().setType(Material.CHEST);
        AirDropUtil.randomFillChest(airDrop, generator.loot());

        hologram = Hologram.create(holoLocation, "&cАирДроп");

        open = true;
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), this::breakChest, 600L);
        Utilities.bcast("&7AirDrop спустился! Координаты: X:" + airDrop.getX() + " Y:" + airDrop.getY() + " Z:" + airDrop.getZ());
    }

    private void breakChest() {
        if (airDrop.getBlock().getState() instanceof Chest) {
            airDrop.getBlock().setType(Material.AIR);
        }
        hologram.clear();
        open = false;
        task = -1;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), AirDrop::start, 24000L);
        // Bukkit.getWorld("world").createExplosion(airDrop, 2.0F, false);
        Utilities.bcast("&cAirDrop разрушился. Следующий появится через 6 часов.");
    }

    public void cooldown() {
        Vec3f loc = new Vec3f(airDrop).add(0.5F, 1.0F, 0.5F);
        holoLocation = new Location(airDrop.getWorld(), loc.x, loc.y, loc.z);
        task = Bukkit.getScheduler().scheduleSyncDelayedTask(Anarchy.getInstance(), this::openChest, 100L);
        //Utilities.bcast(" &7На следующих координатах появится Аирдроп. X:" + airDrop.getX() + " Y:" + airDrop.getY() + " Z:" + airDrop.getZ());
    }

    public void stop() {
        open = false;
        Bukkit.getScheduler().cancelTask(task);
        task = -1;
        breakChest();
    }

    public static void start() {
        instance = new AirDrop(AirDropUtil.generateLocation(), new StandardLootGenerator());
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
