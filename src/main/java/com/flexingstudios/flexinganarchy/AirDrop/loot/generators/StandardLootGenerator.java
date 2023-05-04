package com.flexingstudios.flexinganarchy.AirDrop.loot.generators;

import com.flexingstudios.FlexingNetwork.api.util.Rand;
import com.flexingstudios.flexinganarchy.AirDrop.loot.LootGenerator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StandardLootGenerator extends LootGenerator {
    @Override
    public List<ItemStack> loot() {
        List<ItemStack> items = new ArrayList<>(1);
        ItemStack sharpness = new ItemStack(Material.ENCHANTED_BOOK, 1);
        if (random.nextFloat() < 0.04F) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        }
        if (random.nextFloat() < 0.085F) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
        }
        if (random.nextFloat() < 0.13F) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
        }
        if (random.nextFloat() < 0.3F) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
        }
        if (random.nextFloat() < 0.53F) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        }
        if (sharpness.getEnchantments().isEmpty() || random.nextFloat() < 0.64) {
            sharpness.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        }

        if (random.nextFloat() < 0.7F)
            items.add(sharpness);
        if (random.nextFloat() < 0.9F) {
            items.add(new ItemStack(Material.GOLDEN_APPLE, Rand.intRange(1, 2), (short) 1));
        }

        return items;
    }
}
