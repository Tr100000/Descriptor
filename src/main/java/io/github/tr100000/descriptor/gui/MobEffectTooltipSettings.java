package io.github.tr100000.descriptor.gui;

public record MobEffectTooltipSettings(
        boolean showAmplifier,
        boolean showDuration
) {
    public static final MobEffectTooltipSettings DEFAULT = new MobEffectTooltipSettings(false, false);
}
