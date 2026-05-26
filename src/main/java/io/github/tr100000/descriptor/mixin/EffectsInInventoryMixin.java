package io.github.tr100000.descriptor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.descriptor.DescriptorUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {
    @Inject(method = "extractEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V", shift = At.Shift.AFTER))
    private void extractEffects(GuiGraphicsExtractor graphics, Collection<MobEffectInstance> activeEffects, int x0, int yStep, int mouseX, int mouseY, int maxWidth, CallbackInfo ci, @Local(name = "effect") MobEffectInstance effect, @Local(name = "textureWidth") int textureWidth, @Local(name = "y0") int y0) {
        if (mouseX >= x0 && mouseX < x0 + textureWidth && mouseY >= y0 && mouseY < y0 + 32) {
            String descriptionKey = DescriptorUtil.getMobEffectDescriptionKey(effect.getEffect());
            if (I18n.exists(descriptionKey)) {
                graphics.setTooltipForNextFrame(Component.translatable(descriptionKey), mouseX, mouseY);
            }
        }
    }
}
