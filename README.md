# UtilityMod-PVP

A lightweight, client-side Minecraft PvP utility mod for Fabric.

## Target
- Minecraft Java Edition 26.3
- Fabric Loader 0.20.1+
- Fabric API 0.165.2+26.3
- Java 25

## Features
- Configurable HUD overlay
- Totem, End Crystal, Ender Pearl, Golden Apple and Obsidian counters
- Armor durability summary
- Active potion effect list
- Coordinates, FPS and connection ping
- WASD / sprint / sneak / mouse keystroke display
- Left and right CPS counters
- HUD visibility toggle key
- JSON configuration stored in `.minecraft/config/utilitymod-pvp.json`

The mod is client-side and does not automate combat, move items, alter attack timing, or bypass server anti-cheat.

## Build
Install JDK 25 and Gradle 9.7.1, then run:

```bash
gradle build
```

The distributable mod JAR is written to `build/libs/`.

## Install
1. Install Fabric Loader for Minecraft 26.3.
2. Install the matching Fabric API release.
3. Put the built `utilitymod-pvp-*.jar` into the Minecraft `mods` folder.
4. Launch the Fabric profile.

## Configuration
Launch the game once to generate the config file. Change the feature toggles and HUD layout values, then restart or reload the mod.

## License
MIT
