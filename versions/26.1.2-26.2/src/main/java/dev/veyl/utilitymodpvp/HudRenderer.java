package dev.veyl.utilitymodpvp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public final class HudRenderer {
    private HudRenderer() {}

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        ModConfig config = UtilityModClient.CONFIG;
        if (config == null || !config.enabled || client.player == null || client.level == null) return;

        // Build only the enabled rows. No world scans, entity scans, threads or per-frame timers.
        List<String> lines = new ArrayList<>(10);
        if (config.showCps) {
            lines.add("CPS  L " + UtilityModClient.getLeftCps() + "  R " + UtilityModClient.getRightCps());
        }
        if (config.showItems) {
            int[] counts = countCombatItems(client.player.getInventory());
            lines.add("TOT " + counts[0] + "  CR " + counts[1] + "  PE " + counts[2]);
            lines.add("GAP " + counts[3] + "  OBS " + counts[4]);
        }
        if (config.showArmor) {
            Inventory inventory = client.player.getInventory();
            StringBuilder armor = new StringBuilder("ARM ");
            for (int slot = 39; slot >= 36; slot--) {
                ItemStack stack = inventory.getItem(slot);
                if (stack.isEmpty()) armor.append("-- ");
                else if (stack.isDamageableItem()) {
                    int remaining = 100 - Math.round(stack.getDamageValue() * 100.0f / stack.getMaxDamage());
                    armor.append(remaining).append("% ");
                } else armor.append("100% ");
            }
            lines.add(armor.toString().trim());
        }
        if (config.showEffects && !client.player.getActiveEffects().isEmpty()) {
            StringBuilder effects = new StringBuilder("FX ");
            for (MobEffectInstance effect : client.player.getActiveEffects()) {
                effects.append(effect.getEffect().value().getDisplayName().getString()).append("  ");
            }
            lines.add(effects.toString().trim());
        }
        if (config.showCoordinates) {
            lines.add("XYZ  " + client.player.getBlockX() + "  "
                    + client.player.getBlockY() + "  " + client.player.getBlockZ());
        }
        if (config.showFps) lines.add("FPS  " + client.getFps());
        if (config.showPing && client.getConnection() != null) {
            var info = client.getConnection().getPlayerInfo(client.player.getUUID());
            if (info != null) lines.add("PING  " + info.getLatency() + " ms");
        }
        if (config.showKeystrokes) {
            lines.add("KEYS  " + key(client.options.keyUp.isDown(), "W") + " "
                    + key(client.options.keyLeft.isDown(), "A") + " "
                    + key(client.options.keyDown.isDown(), "S") + " "
                    + key(client.options.keyRight.isDown(), "D")
                    + "   " + key(client.options.keyJump.isDown(), "J")
                    + " " + key(client.options.keySprint.isDown(), "SPR")
                    + " " + key(client.options.keyShift.isDown(), "SH"));
        }

        if (lines.isEmpty()) return;
        int x = config.x;
        int y = config.y;
        int lineHeight = 12;
        int width = 0;
        for (String line : lines) width = Math.max(width, client.font.width(line));
        width += 12;
        int height = lines.size() * lineHeight + 8;

        graphics.pose().pushMatrix();
        graphics.pose().scale(config.scale, config.scale);
        graphics.fill(x, y, x + width, y + height, config.backgroundColor);
        graphics.fill(x, y, x + 2, y + height, config.accentColor);
        int textY = y + 5;
        for (String line : lines) {
            graphics.text(client.font, line, x + 7, textY, config.textColor, false);
            textY += lineHeight;
        }
        graphics.pose().popMatrix();
    }

    private static String key(boolean down, String label) {
        return down ? "[" + label + "]" : label;
    }

    // Count all five tracked combat items in one inventory pass instead of scanning five times.
    private static int[] countCombatItems(Inventory inventory) {
        int[] counts = new int[5];
        for (ItemStack stack : inventory.getNonEquipmentItems()) addCount(counts, stack);
        addCount(counts, inventory.getItem(40));
        return counts;
    }

    private static void addCount(int[] counts, ItemStack stack) {
        if (stack.is(Items.TOTEM_OF_UNDYING)) counts[0] += stack.getCount();
        else if (stack.is(Items.END_CRYSTAL)) counts[1] += stack.getCount();
        else if (stack.is(Items.ENDER_PEARL)) counts[2] += stack.getCount();
        else if (stack.is(Items.GOLDEN_APPLE)) counts[3] += stack.getCount();
        else if (stack.is(Items.OBSIDIAN)) counts[4] += stack.getCount();
    }
}
