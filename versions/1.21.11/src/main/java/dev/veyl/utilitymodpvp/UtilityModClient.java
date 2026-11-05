package dev.veyl.utilitymodpvp;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public final class UtilityModClient implements ClientModInitializer {
    public static final String MOD_ID = "utilitymod_pvp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG;
    private static KeyMapping toggleHudKey, openSettingsKey, toggleSprintKey;
    private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
    private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();
    private static boolean wasAttackDown, wasUseDown, sprintEnabled;

    @Override
    public void onInitializeClient() {
        CONFIG = ModConfig.load();
        sprintEnabled = CONFIG.toggleSprint;
        KeyMapping.Category category = KeyMapping.Category.register(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "general"));
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.utilitymod_pvp.toggle_hud", InputConstants.Type.KEYSYM, InputConstants.KEY_O, category));
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.utilitymod_pvp.open_settings", InputConstants.Type.KEYSYM, InputConstants.KEY_LSHIFT, category));
        toggleSprintKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.utilitymod_pvp.toggle_sprint", InputConstants.Type.KEYSYM, InputConstants.KEY_P, category));
        ClientTickEvents.END_CLIENT_TICK.register(UtilityModClient::onEndTick);
        HudRenderCallback.EVENT.register(HudRenderer::render);
        LOGGER.info("UtilityMod-PVP 1.21.11 initialized.");
    }

    private static void onEndTick(Minecraft client) {
        while (toggleHudKey.consumeClick()) { CONFIG.enabled = !CONFIG.enabled; CONFIG.save(); }
        while (openSettingsKey.consumeClick()) client.setScreen(new UtilitySettingsScreen());
        while (toggleSprintKey.consumeClick()) {
            sprintEnabled = !sprintEnabled; CONFIG.toggleSprint = sprintEnabled; CONFIG.save();
        }
        if (client.player == null) { wasAttackDown = false; wasUseDown = false; return; }
        boolean attackDown = client.options.keyAttack.isDown();
        boolean useDown = client.options.keyUse.isDown();
        long now = System.currentTimeMillis();
        if (attackDown && !wasAttackDown) LEFT_CLICKS.addLast(now);
        if (useDown && !wasUseDown) RIGHT_CLICKS.addLast(now);
        wasAttackDown = attackDown; wasUseDown = useDown;
        trimClicks(LEFT_CLICKS, now); trimClicks(RIGHT_CLICKS, now);
        if (sprintEnabled && client.options.keyUp.isDown() && !client.player.isSprinting()) {
            client.player.setSprinting(true);
        }
    }

    private static void trimClicks(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000L) clicks.removeFirst();
    }
    public static int getLeftCps() { return LEFT_CLICKS.size(); }
    public static int getRightCps() { return RIGHT_CLICKS.size(); }
}
