package dev.developclient.config;

import com.google.gson.*;
import dev.developclient.DevelopClient;
import dev.developclient.module.*;
import dev.developclient.module.Module;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;

public class Config {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("developclient.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("guiKey", DevelopClient.guiKey);
            for (Module m : ModuleManager.MODULES) {
                JsonObject o = new JsonObject();
                o.addProperty("key", m.key);
                for (Setting s : m.settings) o.addProperty(s.name, s.value);
                root.add(m.name, o);
            }
            Files.writeString(FILE, GSON.toJson(root));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            JsonObject root = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();
            if (root.has("guiKey")) DevelopClient.guiKey = root.get("guiKey").getAsInt();
            for (Module m : ModuleManager.MODULES) {
                if (!root.has(m.name)) continue;
                JsonObject o = root.getAsJsonObject(m.name);
                if (o.has("key")) m.key = o.get("key").getAsInt();
                for (Setting s : m.settings) if (o.has(s.name)) s.value = o.get(s.name).getAsDouble();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
