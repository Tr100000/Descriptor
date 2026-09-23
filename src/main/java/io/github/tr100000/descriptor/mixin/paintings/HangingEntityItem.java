package io.github.tr100000.descriptor.mixin.paintings;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import io.github.tr100000.descriptor.gui.ComponentWithTooltip;
import io.github.tr100000.descriptor.gui.PaintingPreviewTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(net.minecraft.world.item.HangingEntityItem.class)
public abstract class HangingEntityItem {
    @Inject(
            method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0)
    )
    private void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag, CallbackInfo ci, @Local(name = "variant") Holder<PaintingVariant> variant) {
        if (DescriptorConfig.check(c -> c.paintings.showPreviewTooltip)) {
            builder.accept(new ComponentWithTooltip(new PaintingPreviewTooltipComponent(variant)));
        }
    }
}
