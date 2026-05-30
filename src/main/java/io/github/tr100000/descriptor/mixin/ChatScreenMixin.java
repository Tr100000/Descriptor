package io.github.tr100000.descriptor.mixin;

import com.google.common.collect.Ordering;
import io.github.tr100000.descriptor.DescriptorUtil;
import io.github.tr100000.descriptor.gui.MobEffectTooltipCache;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Component title) {
        super(title);
        throw new AssertionError();
    }

    @Unique
    private MobEffectTooltipCache tooltipCache;

    @Inject(method = "<init>(Ljava/lang/String;ZZ)V", at = @At("TAIL"))
    private void init(String initial, boolean isDraft, boolean closeOnSubmit, CallbackInfo ci) {
        tooltipCache = new MobEffectTooltipCache();
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        assert minecraft.player != null;
        Collection<MobEffectInstance> activeEffects = minecraft.player.getActiveEffects();
        if (!activeEffects.isEmpty() && !minecraft.options.hideGui) {
            int harmfulCount = 0;
            int beneficialCount = 0;

            for (MobEffectInstance instance : Ordering.natural().reverse().sortedCopy(activeEffects)) {
                Holder<MobEffect> effect = instance.getEffect();

                int x = graphics.guiWidth();
                int y = minecraft.isDemo() ? 16 : 1;

                if (effect.value().isBeneficial()) {
                    beneficialCount++;
                    x -= 25 * beneficialCount;
                } else {
                    harmfulCount++;
                    x -= 25 * harmfulCount;
                    y += 26;
                }

                if (mouseX >= x && mouseX < x + 24 && mouseY >= y && mouseY < y + 24) {
                    DescriptorUtil.extractMobEffectTooltip(graphics, tooltipCache, effect, mouseX, mouseY);
                }
            }
        }
    }
}
