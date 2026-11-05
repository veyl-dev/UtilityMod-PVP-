package dev.veyl.utilitymodpvp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("utilitymod-pvp.json");

    public boolean enabled = true;
    public boolean showCps = true;
    public boolean showItems = true;
    public boolean showArmor = true;
    public boolean showEffects = true;
    public boolean showCoordinates = true;
    public boolean showFps = true;
    public boolean showPing = true;
    public boolean showKeystrokes = true;
    public boolean toggleSprint = false;
    public int x = 8;
    public int y = 8;
    public float scale = 1.0f;
    public int backgroundColor = 0x990D111B;
    public int textColor = 0xFFF2F5FA;
    public int accentColor = 0xFF78E6B1;

    public static ModConfig load() {
        try {
            if (Files.exists(FILE)) {
                ModConfig loaded = GSON.fromJson(Files.readString(FILE), ModConfig.class);
                if (loaded != null) {
                    loaded.scale = Math.max(0.5f, Math.min(2.0f, loaded.scale));
                    loaded.x = Math.max(0, loaded.x);
                    loaded.y = Math.max(0, loaded.y);
                    return loaded;
                }
            }
        } catch (Exception exception) {
            UtilityModClient.LOGGER.error("Could not read config; using defaults.", exception);
        }
        ModConfig defaults = new ModConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(this));
        } catch (IOException exception) {
            UtilityModClient.LOGGER.error("Could not save config.", exception);
        }
    }
}
