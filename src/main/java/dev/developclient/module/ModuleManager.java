package dev.developclient.module;

import dev.developclient.module.impl.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> MODULES = new ArrayList<>();

    public static void init() {
        MODULES.add(new Fullbright());
        MODULES.add(new Zoom());
        MODULES.add(new ChunkFinder());
        MODULES.add(new Sprint());
        MODULES.add(new Fly());
        MODULES.add(new NoFall());
    }

    public static List<Module> byCategory(Category c) {
        return MODULES.stream().filter(m -> m.category == c).toList();
    }

    public static void tick(MinecraftClient mc) {
        for (Module m : MODULES) {
            if (m.key >= 0 && mc.currentScreen == null && mc.player != null) {
                boolean down = InputUtil.isKeyPressed(mc.getWindow(), m.key);
                if (down && !m.keyWasDown) m.toggle();
                m.keyWasDown = down;
            }
            if (m.enabled && mc.player != null && mc.world != null) m.onTick();
        }
    }
}
