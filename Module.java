package dev.developclient.module;

import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    public final String name;
    public final Category category;
    public boolean enabled, expanded;
    public int key = -1;
    public boolean keyWasDown;
    public final List<Setting> settings = new ArrayList<>();

    protected final MinecraftClient mc = MinecraftClient.getInstance();

    protected Module(String name, Category category) { this.name = name; this.category = category; }

    public void toggle() { setEnabled(!enabled); }
    public void setEnabled(boolean e) {
        if (e == enabled) return;
        enabled = e;
        if (e) onEnable(); else onDisable();
    }
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
