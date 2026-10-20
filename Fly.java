package dev.developclient.module.impl;

import dev.developclient.module.*;

public class Fly extends Module {
    private final Setting speed = new Setting("Speed", 0.05, 0.02, 0.5, 0.02);
    public Fly() { super("Fly", Category.MOVEMENT); settings.add(speed); }
    @Override public void onTick() {
        mc.player.getAbilities().allowFlying = true;
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) speed.value);
    }
    @Override public void onDisable() {
        if (mc.player == null) return;
        boolean keep = mc.player.isCreative() || mc.player.isSpectator();
        mc.player.getAbilities().allowFlying = keep;
        if (!keep) mc.player.getAbilities().flying = false;
        mc.player.getAbilities().setFlySpeed(0.05f);
    }
}
