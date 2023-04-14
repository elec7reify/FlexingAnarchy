package com.flexingstudios.anarchy.AirDrop;

import com.flexingstudios.FlexingNetwork.api.entity.NMSEntityUtils;
import com.flexingstudios.FlexingNetwork.api.util.Fireworks;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Particles;
import com.flexingstudios.FlexingNetwork.api.util.Rand;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class AirDropUtil {
    public static Location generateLocation() {
        return generateSquare();
    }

    private static Location generateSquare() {
        //Generate a random X and Z based off the quadrant selected
        int min = 150;
        int max = 175 - min;
        int x, z;
        int quadrant = new Random().nextInt(4);
        try {
            switch (quadrant) {
                case 0: // Positive X and Z
                    x = new Random().nextInt(max) + min;
                    z = new Random().nextInt(max) + min;
                    break;
                case 1: // Negative X and Z
                    x = -new Random().nextInt(max) - min;
                    z = -(new Random().nextInt(max) + min);
                    break;
                case 2: // Negative X and Positive Z
                    x = -new Random().nextInt(max) - min;
                    z = new Random().nextInt(max) + min;
                    break;
                default: // Positive X and Negative Z
                    x = new Random().nextInt(max) + min;
                    z = -(new Random().nextInt(max) + min);
                    break;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        x += 150;
        z += 150;
        //System.out.println(quadrant);
        return new Location(Bukkit.getWorld("world"), x, Bukkit.getWorld("world").getHighestBlockAt(x, z).getY(), z);
    }

    public static void randomFillChest(Location chest, List<ItemStack> items) {
        BlockState state = chest.getBlock().getState();
        if (state instanceof Chest)
            randomFillInventory(((Chest) state).getBlockInventory(), items);
    }

    public static void randomFillInventory(Inventory inv, List<ItemStack> items) {
        ItemStack[] contents = new ItemStack[inv.getSize()];
        int i = 0;
        for (ItemStack item : items)
            contents[i++] = item;
        shuffleArray(contents, Rand.getRandom());
        inv.setContents(contents);
    }

    private static <T> void shuffleArray(T[] arr, Random rnd) {
        for (int i = arr.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            T a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }

    public static void fallingChest(Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0.5D, 10D, 0.5D), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSmall(false);
        armorStand.setGravity(true);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.setMarker(true);
        armorStand.setHelmet(Items.name(Material.CHEST, null, (String) null));
    }
}
