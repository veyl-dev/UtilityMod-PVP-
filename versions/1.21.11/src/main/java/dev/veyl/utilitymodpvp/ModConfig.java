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

    public boolean enabled = true, showCps = true, showItems = true, showArmor = true;
    public boolean showEffects = true, showCoordinates = true, showFps = true, showPing = true;
    public boolean showKeystrokes = true, toggleSprint = false;
    public int x = 8, y = 8;
    public float scale = 1.0f;
    public int backgroundColor = 0x990D111B, textColor = 0xFFF2F5FA, accentColor = 0xFF78E6B1;
    public String profile = "Balanced";

    public static ModConfig load() {
        try {
            if (Files.exists(FILE)) {
                ModConfig loaded = GSON.fromJson(Files.readString(FILE), ModConfig.class);
                if (loaded != null) {
                    loaded.scale = Math.max(0.65f, Math.min(1.75f, loaded.scale));
                    loaded.x = Math.max(0, loaded.x);
                    loaded.y = Math.max(0, loaded.y);
                    if (loaded.profile == null) loaded.profile = "Balanced";
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

    public void applyProfile(String name) {
        profile = name;
        switch (name) {
            case "Minimal" -> {
                showCps = true; showItems = true; showArmor = false; showEffects = false;
                showCoordinates = false; showFps = false; showPing = false; showKeystrokes = false;
            }
            case "Combat" -> {
                showCps = true; showItems = true; showArmor = true; showEffects = true;
                showCoordinates = false; showFps = true; showPing = true; showKeystrokes = true;
            }
            default -> {
                profile = "Balanced";
                showCps = true; showItems = true; showArmor = true; showEffects = true;
                showCoordinates = true; showFps = true; showPing = true; showKeystrokes = true;
            }
        }
        save();
    }

    public void cycleTheme() {
        if (accentColor == 0xFF78E6B1) {
            accentColor = 0xFFB79CFF; backgroundColor = 0x99130D1B; textColor = 0xFFF6F0FF;
        } else if (accentColor == 0xFFB79CFF) {
            accentColor = 0xFF75D7FF; backgroundColor = 0x990A1520; textColor = 0xFFF0FAFF;
        } else {
            accentColor = 0xFF78E6B1; backgroundColor = 0x990D111B; textColor = 0xFFF2F5FA;
        }
        save();
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
