# UtilityMod-PVP

A lightweight, client-side Minecraft PvP utility mod for Fabric.

## Supported Minecraft versions

The repository builds separate JARs for each target. Do not install a JAR on a different Minecraft version just because the mod name is the same.

| Minecraft | Fabric Loader | Fabric API | Java | Build profile |
|---|---:|---:|---:|---|
| 26.3 | 0.20.1 | 0.165.2+26.3 | 25 | Main |
| 26.2 | 0.19.3 | 0.161.0+26.2 | 25 | Main |
| 26.1.2 | 0.19.5 | 0.155.3+26.1.2 | 25 | Main |
| 26.1 | 0.18.4 | 0.143.11+26.1 | 25 | Main |
| 1.21.11 | 0.19.5 | 0.141.6+1.21.11 | 21 | Legacy |

The 26.x targets use Mojang's unobfuscated names and the non-remapping Fabric Loom plugin. Minecraft 1.21.11 remains obfuscated and uses a separate remapping build and legacy source set. A JAR is built independently for each target.

## Features

- Configurable PvP HUD with CPS, combat item counts, armor durability, effects, coordinates, FPS, ping and keystrokes
- In-game settings screen with independent HUD module toggles
- Draggable HUD preview/editor
- HUD scale control and three color themes
- Balanced, Minimal and Combat HUD profiles
- Left/right click rate display
- Optional toggle sprint
- JSON configuration at `.minecraft/config/utilitymod-pvp.json`

## Controls

| Key | Action |
|---|---|
| Left Shift | Open settings / HUD editor |
| O | Toggle the HUD |
| P | Toggle optional sprint |

All key bindings can be changed in Minecraft's Controls menu.

## Lightweight design

- Client-side only; no server plugin or networking required
- No background worker threads, polling loops or world/entity scans
- Combat item counters are collected in one inventory pass
- CPS uses a small rolling click history
- HUD only builds rows for enabled modules
- No attack automation, inventory manipulation, packet spoofing or anti-cheat bypasses

## Build

For 26.x, install JDK 25 and Gradle 9.8.0:

```bash
gradle --no-daemon clean build
```

For Minecraft 1.21.11, run the independent legacy build from `versions/1.21.11` with JDK 21 available:

```bash
gradle --no-daemon clean build
```

The CI workflow builds every target and uploads separate version-labelled artifacts. JARs are written to each profile's `build/libs/` directory.

## Install

1. Install Fabric Loader for the exact Minecraft version you play.
2. Install the matching Fabric API release from the table above.
3. Download the artifact whose Minecraft version matches your game.
4. Put that JAR into the Minecraft `mods` folder.
5. Launch the matching Fabric profile.

## Configuration

Launch the game once to generate the JSON configuration. Most settings can be changed in-game with Left Shift.

## License

MIT
