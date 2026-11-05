package dev.veyl.utilitymodpvp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
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
        if (config == null || !config.enabled || client.player == null || client.level == null || client.options.hideGui) return;

        List<String> lines = new ArrayList<>();
        if (config.showCps) lines.add("CPS  L " + UtilityModClient.getLeftCps() + "  R " + UtilityModClient.getRightCps());
        if (config.showItems) {
            Inventory inv = client.player.getInventory();
            lines.add("TOT " + count(inv, Items.TOTEM_OF_UNDYING)
                    + "  CR " + count(inv, Items.END_CRYSTAL)
                    + "  PE " + count(inv, Items.ENDER_PEARL));
            lines.add("GAP " + count(inv, Items.GOLDEN_APPLE)
                    + "  OBS " + count(inv, Items.OBSIDIAN));
        }
        if (config.showArmor) {
            List<ItemStack> armor = client.player.getInventory().armor;
            StringBuilder armorLine = new StringBuilder("ARM ");
            for (int i = armor.size() - 1; i >= 0; i--) {
                ItemStack stack = armor.get(i);
                if (stack.isEmpty()) armorLine.append("-- ");
                else if (stack.isDamageableItem()) {
                    int remaining = 100 - Math.round(stack.getDamageValue() * 100.0f / stack.getMaxDamage());
                    armorLine.append(remaining).append("% ");
                } else armorLine.append("100% ");
            }
            lines.add(armorLine.toString().trim());
        }
        if (config.showEffects && !client.player.getActiveEffects().isEmpty()) {
            StringBuilder effects = new StringBuilder("FX ");
            for (MobEffectInstance effect : client.player.getActiveEffects()) {
                effects.append(effect.getEffect().value().getDisplayName().getString()).append("  ");
            }
            lines.add(effects.toString().trim());
        }
        if (config.showCoordinates) {
            lines.add(String.format("XYZ  %d  %d  %d",
                    client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ()));
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
            graphics.drawString(client.font, line, x + 7, textY, config.textColor);
            textY += lineHeight;
        }
        graphics.pose().popMatrix();
    }

    private static String key(boolean down, String label) {
        return down ? "[" + label + "]" : label;
    }

    private static int count(Inventory inventory, Item item) {
        int total = 0;
        for (ItemStack stack : inventory.items) if (stack.is(item)) total += stack.getCount();
        for (ItemStack stack : inventory.offhand) if (stack.is(item)) total += stack.getCount();
        return total;
    }
}
