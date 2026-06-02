package io.github.tr100000.descriptor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.descriptor.DescriptorUtil;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import io.github.tr100000.descriptor.gui.MobEffectTooltipCache;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {
    @Unique
    private MobEffectTooltipCache tooltipCache;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(AbstractContainerScreen<?> screen, CallbackInfo ci) {
        tooltipCache = new MobEffectTooltipCache();
    }

    @Inject(method = "extractEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V", shift = At.Shift.AFTER))
    private void extractEffects(GuiGraphicsExtractor graphics, Collection<MobEffectInstance> activeEffects, int x0, int yStep, int mouseX, int mouseY, int maxWidth, CallbackInfo ci, @Local(name = "effect") MobEffectInstance effect, @Local(name = "textureWidth") int textureWidth, @Local(name = "y0") int y0) {
        if (!DescriptorConfig.check(c -> c.mobEffects.showEffectsInInventoryTooltips.getValue()))
            return;

        if (mouseX >= x0 && mouseX < x0 + textureWidth && mouseY >= y0 && mouseY < y0 + 32) {
            DescriptorUtil.extractMobEffectTooltip(graphics, tooltipCache, effect.getEffect(), mouseX, mouseY);
        }
    }
}
