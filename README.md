# UtilityMod-PVP

A lightweight, client-side Minecraft PvP utility mod for Fabric.

## Minecraft version profiles

The project creates a separate JAR for each Minecraft target. Never use a JAR built for a different game version.

| Minecraft Java | Fabric Loader | Fabric API | Java toolchain | Build profile |
|---|---:|---:|---:|---|
| 26.3 | 0.20.1 | 0.165.2+26.3 | 25 | Root |
| 26.2 | 0.19.3 | 0.161.0+26.2 | 25 | `versions/26.2` |
| 26.1.2 | 0.19.5 | 0.155.3+26.1.2 | 25 | `versions/26.1.2-26.2` |
| 26.1 | 0.18.4 | 0.143.11+26.1 | 25 | `versions/26.1` |
| 1.21.11 | 0.19.5 | 0.141.6+1.21.11 | 21 | `versions/1.21.11` |

The 26.x releases use Mojang's unobfuscated names and the non-remapping Loom plugin. Minecraft 1.21.11 is still obfuscated and uses a separate remapping Loom project with a Java 21 compile toolchain. Gradle itself runs on Java 25 because the pinned current Loom plugin requires it.

The 26.1 and 26.1.2 targets are intentionally separate: Minecraft 26.1 introduced the unobfuscated game and major rendering/API changes, and 26.1.2 received a later Fabric API line. The 26.1 profile registers its HUD through a small client mixin; the 26.1.2/26.2 profile uses the Fabric HUD registry.

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

Use JDK 25 and Gradle 9.8.0 for the Gradle runtime. The 1.21.11 project also configures a Java 21 toolchain for compilation.

For Minecraft 26.3:

```bash
gradle --no-daemon clean build
```

For Minecraft 26.2:

```bash
cd versions/26.2
gradle --no-daemon clean build
```

For Minecraft 26.1.2, run from `versions/26.1.2-26.2` with the 26.1.2 properties:

```bash
cd versions/26.1.2-26.2
gradle --no-daemon clean build -Pminecraft_version=26.1.2 -Ploader_version=0.19.5 -Pfabric_api_version=0.155.3+26.1.2 -Pfabric_api_min=0.155.3
```

For Minecraft 26.1:

```bash
cd versions/26.1
gradle --no-daemon clean build
```

For Minecraft 1.21.11:

```bash
cd versions/1.21.11
gradle --no-daemon clean build
```

GitHub Actions builds every target independently and uploads a version-labelled artifact for each successful build. The CI matrix is the compatibility gate; a target should be considered supported only after its corresponding build passes.

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
