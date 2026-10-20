package dev.developclient.module;
public class Setting {
    public final String name;
    public double value, min, max, step;
    public Setting(String name, double value, double min, double max, double step) {
        this.name = name; this.value = value; this.min = min; this.max = max; this.step = step;
    }
    public void change(int dir) { value = Math.max(min, Math.min(max, value + dir * step)); }
}
