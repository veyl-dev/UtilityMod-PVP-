package dev.veyl.utilitymodpvp.mixin;

import dev.veyl.utilitymodpvp.HudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class HudRenderMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void utilitymodpvp$renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudRenderer.render(graphics, deltaTracker);
    }
}
