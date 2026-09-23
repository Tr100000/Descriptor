package io.github.tr100000.descriptor.mixin;

import io.github.tr100000.descriptor.gui.ComponentWithTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {
    @Inject(
            method = "create(Lnet/minecraft/util/FormattedCharSequence;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void create(FormattedCharSequence charSequence, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (charSequence instanceof ComponentWithTooltip.Formatted(ClientTooltipComponent tooltipComponent)) {
            cir.setReturnValue(tooltipComponent);
        }
    }
}
