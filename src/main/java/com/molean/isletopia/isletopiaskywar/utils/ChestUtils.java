package com.molean.isletopia.isletopiaskywar.utils;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class ChestUtils {

    public static boolean isFull(Inventory chest) {
        for (ItemStack content : chest.getContents()) {
            if (content == null) {
                return false;
            }
            if (content.getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }

    public static void randomlySplitItem(List<Inventory> inventories, List<ItemStack> itemStacks) {

        Random random = new Random();

        for (ItemStack itemStack : itemStacks) {

            inventories.removeIf(ChestUtils::isFull);

            if (inventories.isEmpty()) {
                throw new RuntimeException("no available space");
            }

            Inventory inventory = inventories.get(random.nextInt(inventories.size()));
            inventory.addItem(itemStack);
        }
    }
}
