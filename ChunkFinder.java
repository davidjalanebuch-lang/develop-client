package dev.developclient.module.impl;

import dev.developclient.module.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Scannt geladene Chunks nach interessanten Blöcken und zeigt Treffer im HUD. */
public class ChunkFinder extends Module {
    public record Hit(BlockPos pos, int count) {}
    public static final Map<ChunkPos, Hit> RESULTS = new ConcurrentHashMap<>();
    private static final Set<Block> TARGETS = Set.of(
            Blocks.SPAWNER, Blocks.TRIAL_SPAWNER, Blocks.VAULT, Blocks.ANCIENT_DEBRIS, Blocks.BEACON);

    private final Setting range = new Setting("Range", 8, 2, 32, 1);

    public ChunkFinder() { super("ChunkFinder", Category.WORLD); settings.add(range); }

    @Override public void onDisable() { RESULTS.clear(); }

    @Override public void onTick() {
        if (mc.player.age % 40 != 0) return;
        RESULTS.clear();
        ChunkPos me = mc.player.getChunkPos();
        int r = (int) range.value;
        for (int dx = -r; dx <= r; dx++) for (int dz = -r; dz <= r; dz++) {
            WorldChunk ch = mc.world.getChunkManager().getWorldChunk(me.x + dx, me.z + dz);
            if (ch == null) continue;
            int count = 0; BlockPos first = null;
            ChunkSection[] sections = ch.getSectionArray();
            for (int i = 0; i < sections.length; i++) {
                ChunkSection s = sections[i];
                if (s == null || s.isEmpty() || !s.hasAny(st -> TARGETS.contains(st.getBlock()))) continue;
                int baseY = ch.sectionIndexToCoord(i) * 16;
                for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {
                    if (TARGETS.contains(s.getBlockState(x, y, z).getBlock())) {
                        count++;
                        if (first == null) first = new BlockPos(ch.getPos().getStartX() + x, baseY + y, ch.getPos().getStartZ() + z);
                    }
                }
            }
            if (count > 0) RESULTS.put(ch.getPos(), new Hit(first, count));
        }
    }
}
