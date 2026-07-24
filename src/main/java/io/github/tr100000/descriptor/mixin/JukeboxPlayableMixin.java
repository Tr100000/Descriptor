package io.github.tr100000.descriptor.mixin;

import io.github.tr100000.descriptor.config.DescriptorConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.util.function.Consumer;

@Mixin(JukeboxPlayable.class)
public abstract class JukeboxPlayableMixin {
    @Shadow @Final
    private Holder<JukeboxSong> song;

    @Inject(method = "addToTooltip", at = @At("TAIL"))
    private void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components, CallbackInfo ci) {
        if (DescriptorConfig.check(c -> c.musicDiscs.showLengthTooltip.getValue())) {
            Duration duration = Duration.ofSeconds(Mth.ceil(song.value().lengthInSeconds()));
            String durationStr = formatDurationStr(duration);
            consumer.accept(Component.translatable("descriptor.music_disc.length", durationStr).withColor(TextColor.GRAY));
        }
        if (DescriptorConfig.check(c -> c.musicDiscs.showRedstoneOutputTooltip.getValue())) {
            consumer.accept(Component.translatable("descriptor.music_disc.comparator_output", song.value().comparatorOutput()).withColor(TextColor.GRAY));
        }
    }

    @Unique
    private String formatDurationStr(Duration duration) {
        if (duration.toHours() > 0) {
            return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        else {
            return String.format("%02d:%02d", duration.toMinutes(), duration.toSecondsPart());
        }
    }
}
