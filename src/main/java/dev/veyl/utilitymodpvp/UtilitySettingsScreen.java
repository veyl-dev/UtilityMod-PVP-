package dev.veyl.utilitymodpvp;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class UtilitySettingsScreen extends Screen {
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public UtilitySettingsScreen() {
        super(Component.literal("UtilityMod-PVP Settings"));
    }

    @Override
    protected void init() {
        int left = 12;
        int top = this.height - 112;
        int width = Math.min(150, (this.width - 36) / 3);
        int gap = 6;

        addToggle("CPS", () -> UtilityModClient.CONFIG.showCps = !UtilityModClient.CONFIG.showCps, left, top, width);
        addToggle("Items", () -> UtilityModClient.CONFIG.showItems = !UtilityModClient.CONFIG.showItems, left + width + gap, top, width);
        addToggle("Armor", () -> UtilityModClient.CONFIG.showArmor = !UtilityModClient.CONFIG.showArmor, left + (width + gap) * 2, top, width);
        addToggle("Effects", () -> UtilityModClient.CONFIG.showEffects = !UtilityModClient.CONFIG.showEffects, left, top + 25, width);
        addToggle("Coordinates", () -> UtilityModClient.CONFIG.showCoordinates = !UtilityModClient.CONFIG.showCoordinates, left + width + gap, top + 25, width);
        addToggle("FPS", () -> UtilityModClient.CONFIG.showFps = !UtilityModClient.CONFIG.showFps, left + (width + gap) * 2, top + 25, width);
        addToggle("Ping", () -> UtilityModClient.CONFIG.showPing = !UtilityModClient.CONFIG.showPing, left, top + 50, width);
        addToggle("Keystrokes", () -> UtilityModClient.CONFIG.showKeystrokes = !UtilityModClient.CONFIG.showKeystrokes, left + width + gap, top + 50, width);
        addToggle("HUD", () -> UtilityModClient.CONFIG.enabled = !UtilityModClient.CONFIG.enabled, left + (width + gap) * 2, top + 50, width);

        addRenderableWidget(Button.builder(Component.literal("Scale -"), b -> changeScale(-0.1f))
                .bounds(left, top + 77, width, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Scale +"), b -> changeScale(0.1f))
                .bounds(left + width + gap, top + 77, width, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Theme"), b -> UtilityModClient.CONFIG.cycleTheme())
                .bounds(left + (width + gap) * 2, top + 77, width, 20).build());

        int right = this.width - 162;
        addRenderableWidget(Button.builder(Component.literal("Profile: " + UtilityModClient.CONFIG.profile), b -> {
            String next = switch (UtilityModClient.CONFIG.profile) {
                case "Balanced" -> "Minimal";
                case "Minimal" -> "Combat";
                default -> "Balanced";
            };
            UtilityModClient.CONFIG.applyProfile(next);
            this.clearWidgets();
            this.init();
        }).bounds(right, 12, 150, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Reset position"), b -> {
            UtilityModClient.CONFIG.x = 8;
            UtilityModClient.CONFIG.y = 48;
            UtilityModClient.CONFIG.save();
        }).bounds(right, 37, 150, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(right, 62, 150, 20).build());
    }

    private void addToggle(String label, Runnable action, int x, int y, int width) {
        addRenderableWidget(Button.builder(Component.literal(label + ": " + state(label)), button -> {
            action.run();
            UtilityModClient.CONFIG.save();
            button.setMessage(Component.literal(label + ": " + state(label)));
        }).bounds(x, y, width, 20).build());
    }

    private String state(String label) {
        var c = UtilityModClient.CONFIG;
        boolean enabled = switch (label) {
            case "CPS" -> c.showCps;
            case "Items" -> c.showItems;
            case "Armor" -> c.showArmor;
            case "Effects" -> c.showEffects;
            case "Coordinates" -> c.showCoordinates;
            case "FPS" -> c.showFps;
            case "Ping" -> c.showPing;
            case "Keystrokes" -> c.showKeystrokes;
            default -> c.enabled;
        };
        return enabled ? "ON" : "OFF";
    }

    private void changeScale(float amount) {
        UtilityModClient.CONFIG.scale = Math.max(0.65f, Math.min(1.75f, UtilityModClient.CONFIG.scale + amount));
        UtilityModClient.CONFIG.save();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.fill(0, 0, this.width, this.height, 0x88000000);
        graphics.text(this.font, "UTILITYMOD-PVP  /  HUD EDITOR", 12, 12, 0xFFFFFFFF, true);
        graphics.text(this.font, "Drag the preview panel anywhere above the controls.", 12, 29, 0xFFB8C5D8, false);

        ModConfig c = UtilityModClient.CONFIG;
        int previewWidth = 220;
        int previewHeight = 74;
        int px = Math.max(0, Math.min(this.width - previewWidth, c.x));
        int py = Math.max(48, Math.min(this.height - 204, c.y));
        graphics.fill(px, py, px + previewWidth, py + previewHeight, c.backgroundColor);
        graphics.fill(px, py, px + 3, py + previewHeight, c.accentColor);
        graphics.text(this.font, "CPS  L 6  R 4", px + 10, py + 9, c.textColor, false);
        graphics.text(this.font, "TOT 2  CR 16  PE 8", px + 10, py + 23, c.textColor, false);
        graphics.text(this.font, "ARM 100% 92% 88% 100%", px + 10, py + 37, c.textColor, false);
        graphics.text(this.font, "FPS 144  PING 32 ms", px + 10, py + 51, c.textColor, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ModConfig c = UtilityModClient.CONFIG;
        int px = Math.max(0, Math.min(this.width - 220, c.x));
        int py = Math.max(48, Math.min(this.height - 204, c.y));
        if (button == 0 && mouseX >= px && mouseX <= px + 220 && mouseY >= py && mouseY <= py + 74) {
            dragging = true;
            dragOffsetX = (int) mouseX - px;
            dragOffsetY = (int) mouseY - py;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && button == 0) {
            UtilityModClient.CONFIG.x = Math.max(0, Math.min(this.width - 220, (int) mouseX - dragOffsetX));
            UtilityModClient.CONFIG.y = Math.max(48, Math.min(this.height - 204, (int) mouseY - dragOffsetY));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            UtilityModClient.CONFIG.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        UtilityModClient.CONFIG.save();
        this.minecraft.gui.setScreen(null);
    }
}
