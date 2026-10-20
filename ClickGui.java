package dev.developclient.gui;

import dev.developclient.config.Config;
import dev.developclient.module.*;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Screen {
    private static final int W = 112, H = 16, TOP = 24;
    private Module listening;

    private interface Row { void visit(Module m, Setting s, int x, int y); }

    public ClickGui() { super(Text.literal("Develop Client")); }

    private void layout(Row row) {
        int x = 24;
        for (Category cat : Category.values()) {
            int y = TOP + H + 2;
            for (Module m : ModuleManager.byCategory(cat)) {
                row.visit(m, null, x, y); y += H;
                if (m.expanded) for (Setting s : m.settings) { row.visit(m, s, x, y); y += H; }
            }
            x += W + 8;
        }
    }

    @Override
    public void render(DrawContext c, int mx, int my, float delta) {
        c.fillGradient(0, 0, width, height, 0xA0120A28, 0xA0101C46);
        int x = 24;
        for (Category cat : Category.values()) {
            c.fillGradient(x, TOP, x + W, TOP + H + 2, Theme.PURPLE, Theme.BLUE);
            c.drawText(textRenderer, cat.name(), x + 5, TOP + 5, Theme.WHITE, true);
            x += W + 8;
        }
        layout((m, s, rx, ry) -> {
            boolean hover = mx >= rx && mx < rx + W && my >= ry && my < ry + H;
            if (s == null) {
                c.fill(rx, ry, rx + W, ry + H, m.enabled ? Theme.ACTIVE : hover ? Theme.HOVER : Theme.PANEL);
                c.drawText(textRenderer, m.name, rx + 5, ry + 4, Theme.WHITE, true);
                String k = listening == m ? "[...]" : m.key < 0 ? "" : InputUtil.fromKeyCode(new KeyInput(m.key, 0, 0)).getLocalizedText().getString();
                c.drawText(textRenderer, k, rx + W - textRenderer.getWidth(k) - 4, ry + 4, 0xFFB8A8FF, true);
            } else {
                c.fill(rx, ry, rx + W, ry + H, hover ? Theme.HOVER : 0xE00F0B20);
                c.drawText(textRenderer, s.name + ": " + String.format("%.2f", s.value), rx + 10, ry + 4, 0xFFA5C4FF, true);
            }
        });
        c.drawText(textRenderer, "Links: an/aus  |  Rechts: Einstellungen  |  Mitte: Keybind (ENTF = entfernen)",
                24, height - 14, 0xFFB8A8FF, true);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mx = click.x(), my = click.y();
        int button = click.button();
        boolean[] handled = {false};
        layout((m, s, rx, ry) -> {
            if (handled[0] || mx < rx || mx >= rx + W || my < ry || my >= ry + H) return;
            handled[0] = true;
            if (s != null) { s.change(button == 1 ? -1 : 1); return; }
            if (button == 0) m.toggle();
            else if (button == 1) m.expanded = !m.expanded;
            else if (button == 2) listening = m;
        });
        return handled[0] || super.mouseClicked(click, doubled);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (listening != null) {
            int k = input.key();
            listening.key = (k == GLFW.GLFW_KEY_DELETE || k == GLFW.GLFW_KEY_ESCAPE) ? -1 : k;
            listening = null;
            return true;
        }
        return super.keyPressed(input);
    }

    @Override public void close() { Config.save(); super.close(); }
    @Override public boolean shouldPause() { return false; }
}
