package com.flexingstudios.anarchy.AirDrop.loot;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public abstract class LootGenerator {
    protected Random random;

    public LootGenerator() {
        random = new Random();
    }

    public abstract List<ItemStack> loot();
}
