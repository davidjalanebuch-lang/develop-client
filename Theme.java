package dev.developclient.gui;

public class Theme {
    public static final int PURPLE = 0xFF8B5CF6;
    public static final int BLUE = 0xFF3B82F6;
    public static final int PANEL = 0xE0181330;
    public static final int HOVER = 0xE02A2250;
    public static final int ACTIVE = 0xFF5B3FD0;
    public static final int WHITE = 0xFFFFFFFF;

    public static int lerp(int a, int b, float t) {
        int r = (int) (((a >> 16) & 255) * (1 - t) + ((b >> 16) & 255) * t);
        int g = (int) (((a >> 8) & 255) * (1 - t) + ((b >> 8) & 255) * t);
        int bl = (int) ((a & 255) * (1 - t) + (b & 255) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }
}
