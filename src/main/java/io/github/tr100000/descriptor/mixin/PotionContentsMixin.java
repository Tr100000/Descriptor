package io.github.tr100000.descriptor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tr100000.descriptor.DescriptorUtil;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(PotionContents.class)
public abstract class PotionContentsMixin {
    @Inject(method = "addPotionTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER))
    private static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> lines, float durationScale, float tickrate, CallbackInfo ci, @Local(name = "mobEffect") Holder<MobEffect> mobEffect) {
        if (DescriptorConfig.check(c -> c.mobEffects.showItemTooltips.getValue())) {
            lines.accept(DescriptorUtil.getMobEffectDescription(mobEffect));
        }
    }
}
