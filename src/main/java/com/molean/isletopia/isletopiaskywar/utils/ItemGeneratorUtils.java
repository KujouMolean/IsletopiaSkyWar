package com.molean.isletopia.isletopiaskywar.utils;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static org.bukkit.Material.*;

public class ItemGeneratorUtils {

    private static final Random RANDOM = new Random();

    public static ArrayList<ItemStack> generate(Material material, int amount, int possible) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        if (RANDOM.nextInt(10000) < possible) {
            itemStacks.add(new ItemStack(material, amount));
        }
        return itemStacks;
    }

    public static List<ItemStack> generate(int amount, int possible, Material... materials) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        int count = 0;

        Map<Material, Integer> map = new HashMap<>();
        if (RANDOM.nextInt(10000) < possible) {
            while (count < amount) {
                for (int i = 0; i < materials.length && count < amount; i++) {
                    Material material = materials[RANDOM.nextInt(materials.length)];
                    Integer orDefault = map.getOrDefault(material, 0);
                    map.put(material, orDefault + 1);
                    count++;
                }
            }
            map.forEach((material, integer) -> {
                ItemStack itemStack = new ItemStack(material, integer);
                itemStacks.add(itemStack);
            });

        }
        return itemStacks;
    }

    public static List<ItemStack> generateRedstoneKit() {

        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(REDSTONE_BLOCK, 64));
        itemStacks.add(new ItemStack(PISTON, 64));
        itemStacks.add(new ItemStack(STICKY_PISTON, 64));
        itemStacks.add(new ItemStack(HOPPER, 64));
        itemStacks.add(new ItemStack(SLIME_BLOCK, 64));
        itemStacks.add(new ItemStack(HONEY_BLOCK, 64));
        itemStacks.add(new ItemStack(MINECART, 64));
        itemStacks.add(new ItemStack(HOPPER_MINECART, 64));
        itemStacks.add(new ItemStack(DETECTOR_RAIL, 64));
        itemStacks.add(new ItemStack(DEAD_BRAIN_CORAL_FAN, 64));
        itemStacks.add(new ItemStack(REDSTONE_TORCH, 64));
        itemStacks.add(new ItemStack(DISPENSER, 64));
        itemStacks.add(new ItemStack(DROPPER, 64));
        itemStacks.add(new ItemStack(OBSERVER, 64));
        itemStacks.add(new ItemStack(LEVER, 64));
        itemStacks.add(new ItemStack(TRIPWIRE_HOOK, 64));
        itemStacks.add(new ItemStack(STRING, 64));
        itemStacks.add(new ItemStack(REPEATER, 64));
        itemStacks.add(new ItemStack(COMPARATOR, 64));
        itemStacks.add(new ItemStack(REDSTONE_LAMP, 64));
        itemStacks.add(new ItemStack(TNT, 4));
        return itemStacks;
    }

    public static List<ItemStack> generateVillageKit() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(VILLAGER_SPAWN_EGG, 4));
        itemStacks.add(new ItemStack(EMERALD_BLOCK, 64));
        return itemStacks;
    }

    public static List<ItemStack> generateHorseKit() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(HORSE_SPAWN_EGG, 4));
        itemStacks.add(new ItemStack(EMERALD_BLOCK, 64));
        return itemStacks;
    }

    public static List<ItemStack> generateWitherKit() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(WITHER_SKELETON_SKULL, 1));
        itemStacks.add(new ItemStack(SOUL_SAND, 4));

        return itemStacks;
    }

    public static List<ItemStack> generateElytraKit() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(ELYTRA, 4));
        itemStacks.add(new ItemStack(FIREWORK_ROCKET, 64));
        itemStacks.add(new ItemStack(FIREWORK_ROCKET, 64));
        itemStacks.add(new ItemStack(FIREWORK_ROCKET, 64));
        itemStacks.add(new ItemStack(FIREWORK_ROCKET, 64));
        return itemStacks;
    }

    public static List<ItemStack> generateEnchanmentTableKit() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(new ItemStack(ENCHANTING_TABLE, 1));
        itemStacks.add(new ItemStack(BOOKSHELF, 8 + RANDOM.nextInt(8)));
        return itemStacks;
    }

    public static List<ItemStack> generateStage1() {
        List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(generate(16, 10000, ACACIA_SAPLING, BIRCH_SAPLING, JUNGLE_SAPLING, DARK_OAK_SAPLING, SPRUCE_SAPLING));
        itemStackList.addAll(generate(CHICKEN_SPAWN_EGG, 2, 10000));
        itemStackList.addAll(generate(SHEEP_SPAWN_EGG, 2, 10000));
        itemStackList.addAll(generate(PIG_SPAWN_EGG, 2, 10000));
        itemStackList.addAll(generate(COW_SPAWN_EGG, 2, 10000));
        itemStackList.addAll(generate(GRASS_BLOCK, 64, 10000));
        itemStackList.addAll(generate(GRASS_BLOCK, 64, 10000));
        itemStackList.addAll(generate(WATER_BUCKET, 1, 10000));
        itemStackList.addAll(generate(LAVA_BUCKET, 1, 10000));
        itemStackList.addAll(generate(FISHING_ROD, 1, 10000));
        itemStackList.addAll(generate(FISHING_ROD, 1, 10000));
        itemStackList.addAll(generate(ANVIL, 1, 10000));
        itemStackList.addAll(generate(SPYGLASS, 1, 10000));
        itemStackList.addAll(generate(SPYGLASS, 1, 10000));

        itemStackList.addAll(generate(16, 10000, LEATHER));
        itemStackList.addAll(generate(16, 10000, LEATHER));

        //possible item
        itemStackList.addAll(generate(SHULKER_BOX, 4, 200));
        itemStackList.addAll(generate(32, 1500, MELON_SEEDS, PUMPKIN_SEEDS, CARROT, POTATO, BEETROOT_SEEDS, SWEET_BERRIES, NETHER_WART));
        itemStackList.addAll(generate(16, 5000, ENDER_PEARL));

        if (RANDOM.nextInt(100) < 20) {
            itemStackList.addAll(generateRedstoneKit());
        }

        if (RANDOM.nextInt(100) < 30) {
            itemStackList.addAll(generateVillageKit());
        }

        if (RANDOM.nextInt(100) < 2) {
            itemStackList.addAll(generateWitherKit());
        }

        itemStackList.addAll(generate(4, 1000, TOTEM_OF_UNDYING));

        //todo enchanment book
        itemStackList.addAll(generate(1, 2500, PIGLIN_SPAWN_EGG));
        if (RANDOM.nextInt(100) < 1) {
            itemStackList.addAll(generateElytraKit());
        }
        if (RANDOM.nextInt(100) < 15) {
            itemStackList.addAll(generateEnchanmentTableKit());
        }
        itemStackList.addAll(generate(1, 1000, ENDER_CHEST));

        if (RANDOM.nextInt(100) < 30) {
            itemStackList.addAll(generateHorseKit());
        }


        itemStackList.addAll(generate(4, 500, RABBIT_FOOT));
        itemStackList.addAll(generate(4, 500, SPIDER_EYE));
        itemStackList.addAll(generate(4, 500, GHAST_TEAR));
        itemStackList.addAll(generate(4, 500, MAGMA_CREAM));
        itemStackList.addAll(generate(4, 500, SCUTE));
        return itemStackList;
    }

    public static List<ItemStack> generateStage2() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        itemStacks.add(new ItemStack(TNT, 4));
        itemStacks.add(new ItemStack(ENDER_PEARL, 4));
        itemStacks.add(new ItemStack(DIAMOND, 4));
        itemStacks.add(new ItemStack(ENCHANTING_TABLE, 1));
        itemStacks.add(new ItemStack(BOOKSHELF, 4));
        itemStacks.add(new ItemStack(BREWING_STAND, 1));
        itemStacks.add(new ItemStack(BLAZE_ROD, 16));
        itemStacks.add(new ItemStack(NETHER_WART, 16));
        itemStacks.add(new ItemStack(EXPERIENCE_BOTTLE, 64));
        itemStacks.add(new ItemStack(IRON_INGOT, 16));
        itemStacks.add(new ItemStack(LAPIS_LAZULI, 32));
        ItemStack itemStack = new ItemStack(POTION, 1);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3 * 60 * 20, 0), true);
        itemStack.setItemMeta(potionMeta);
        itemStacks.add(itemStack);
        itemStacks.add(new ItemStack(BOW));
        return itemStacks;
    }

    public static List<ItemStack> generateStage3() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.addAll(generate(EXPERIENCE_BOTTLE, 128, 10000));
        itemStacks.addAll(generate(ENDER_PEARL, 4, 10000));
        itemStacks.addAll(generate(4, 10000, DIAMOND));
        itemStacks.addAll(generate(1, 2500, DIAMOND_AXE));
        itemStacks.addAll(generate(1, 2500, DIAMOND_SWORD));
        itemStacks.addAll(generate(1, 5000, BOOKSHELF));
        itemStacks.addAll(generate(1, 1000, NETHERITE_INGOT));
        itemStacks.addAll(generate(1, 1000, TOTEM_OF_UNDYING));
        itemStacks.addAll(generate(16, 2000, IRON_INGOT));
        itemStacks.addAll(generate(1, 10000, SWEET_BERRIES));
        itemStacks.addAll(generate(4, 2000, SPECTRAL_ARROW));
        itemStacks.addAll(generate(1, 1000, TRIDENT));
        itemStacks.addAll(generate(64, 2000, FIREWORK_ROCKET));
        itemStacks.addAll(generate(10, 2000, RED_MUSHROOM,BROWN_MUSHROOM));
        itemStacks.addAll(generate(10, 1000, DIAMOND_HORSE_ARMOR, GOLDEN_HORSE_ARMOR, IRON_HORSE_ARMOR, LEATHER_HORSE_ARMOR));

        ItemStack itemStack = new ItemStack(POTION, 1);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3 * 60 * 20, 0), true);
        itemStack.setItemMeta(potionMeta);
        itemStacks.add(itemStack);
        return itemStacks;

    }
}
