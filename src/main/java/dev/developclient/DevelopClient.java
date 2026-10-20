package dev.developclient;

import dev.developclient.config.Config;
import dev.developclient.gui.ClickGui;
import dev.developclient.gui.Hud;
import dev.developclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class DevelopClient implements ClientModInitializer {
    public static final String NAME = "Develop Client";
    public static int guiKey = GLFW.GLFW_KEY_RIGHT_SHIFT;
    private boolean guiKeyDown;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Config.load();

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(c -> Config.save());
        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT,
                Identifier.of("developclient", "hud"), (ctx, tickCounter) -> Hud.render(ctx));
    }

    private void tick(MinecraftClient mc) {
        boolean down = InputUtil.isKeyPressed(mc.getWindow(), guiKey);
        if (down && !guiKeyDown && mc.currentScreen == null && mc.player != null) {
            mc.setScreen(new ClickGui());
        }
        guiKeyDown = down;
        ModuleManager.tick(mc);
    }
}
