package dev.developclient.module.impl;

import dev.developclient.module.*;
import dev.developclient.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", Category.MOVEMENT); }
    @Override public void onTick() {
        if (mc.player.forwardSpeed > 0 && !mc.player.horizontalCollision) mc.player.setSprinting(true);
    }
}
