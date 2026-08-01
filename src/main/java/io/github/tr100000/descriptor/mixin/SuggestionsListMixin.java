package io.github.tr100000.descriptor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.Message;
import io.github.tr100000.descriptor.gui.MessageWithCustomTooltip;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin {
    @Shadow
    @Final
    CommandSuggestions this$0;

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;setTooltipForNextFrame(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;II)V"), cancellable = true)
    private void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CallbackInfo ci, @Local Message tooltip) {
        if (tooltip instanceof MessageWithCustomTooltip customTooltip) {
            ci.cancel();

            graphics.setTooltipForNextFrameInternal(this$0.font, customTooltip.tooltip(), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null, false);
        }
    }
}
