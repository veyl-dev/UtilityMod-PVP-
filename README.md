# UtilityMod-PVP

A lightweight, client-side Minecraft PvP utility mod for Fabric.

## Target
- Minecraft Java Edition 26.3
- Fabric Loader 0.20.1+
- Fabric API 0.165.2+26.3
- Java 25

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
| Right Shift | Open settings / HUD editor |
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
Install JDK 25 and Gradle 9.8.0, then run:

```bash
gradle --no-daemon clean build
```

The distributable JAR is written to `build/libs/`.

## Install
1. Install Fabric Loader for Minecraft 26.3.
2. Install Fabric API 0.165.2+26.3 or a newer compatible 26.3 release.
3. Put the built `utilitymod-pvp-*.jar` into the Minecraft `mods` folder.
4. Launch the Fabric profile.

## Configuration
Launch the game once to generate the JSON configuration. Most settings can be changed in-game with Right Shift.

## License
MIT
