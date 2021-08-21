package com.molean.isletopia.isletopiaskywar.objects;

import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class RichBlockData {
    int x;
    int y;
    int z;
    BlockData blockData;
    Biome biome;

    public RichBlockData(int x, int y, int z, BlockData blockData, Biome biome) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockData = blockData;
        this.biome = biome;
    }
}