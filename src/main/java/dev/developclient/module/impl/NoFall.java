package dev.developclient.module.impl;

import dev.developclient.module.*;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() { super("NoFall", Category.PLAYER); }
    @Override public void onTick() {
        if (mc.player.fallDistance > 2f && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, mc.player.horizontalCollision));
        }
    }
}
