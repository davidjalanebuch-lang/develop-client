package dev.developclient.module.impl;

import dev.developclient.module.*;

public class Zoom extends Module {
    private int oldFov = 70;
    private final Setting fov = new Setting("FOV", 30, 30, 70, 5);
    public Zoom() { super("Zoom", Category.RENDER); settings.add(fov); }
    @Override public void onEnable() { oldFov = mc.options.getFov().getValue(); }
    @Override public void onTick() { mc.options.getFov().setValue((int) fov.value); }
    @Override public void onDisable() { mc.options.getFov().setValue(oldFov); }
}
