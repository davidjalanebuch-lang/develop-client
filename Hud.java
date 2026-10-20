package dev.developclient.gui;

import dev.developclient.DevelopClient;
import dev.developclient.module.*;
import dev.developclient.module.impl.ChunkFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.ChunkPos;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Hud {
    public static void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        TextRenderer tr = mc.textRenderer;

        ctx.drawText(tr, DevelopClient.NAME, 4, 4, Theme.PURPLE, true);
        ctx.drawText(tr, String.format("XYZ %.1f / %.1f / %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()),
                4, 16, Theme.WHITE, true);

        // Arraylist rechts oben
        List<Module> active = ModuleManager.MODULES.stream().filter(m -> m.enabled)
                .sorted(Comparator.comparingInt((Module m) -> -tr.getWidth(m.name))).toList();
        int w = mc.getWindow().getScaledWidth(), y = 4, i = 0;
        for (Module m : active) {
            int col = Theme.lerp(Theme.PURPLE, Theme.BLUE, active.size() <= 1 ? 0 : i / (float) (active.size() - 1));
            ctx.drawText(tr, m.name, w - tr.getWidth(m.name) - 4, y, col, true);
            y += 10; i++;
        }

        // ChunkFinder-Ergebnisse
        int cy = 32;
        for (Map.Entry<ChunkPos, ChunkFinder.Hit> e : ChunkFinder.RESULTS.entrySet()) {
            var p = e.getValue().pos();
            ctx.drawText(tr, "Chunk " + e.getKey().x + "," + e.getKey().z + "  ~" + p.getX() + " " + p.getY() + " " + p.getZ()
                    + "  (" + e.getValue().count() + ")", 4, cy, Theme.BLUE, true);
            cy += 10;
            if (cy > 150) break;
        }
    }
}
