package io.github.tr100000.descriptor.integration;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import io.github.tr100000.descriptor.config.option.AbstractOption;
import io.github.tr100000.descriptor.config.option.impl.BooleanOption;
import io.github.tr100000.descriptor.config.option.impl.FloatOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

import static io.github.tr100000.descriptor.config.DescriptorConfig.INSTANCE;
import static io.github.tr100000.descriptor.config.DescriptorConfig.configTranslated;

public final class DescriptorYaclConfig {
    private DescriptorYaclConfig() {}

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(configTranslated("title"))
                .category(ConfigCategory.createBuilder()
                        .name(configTranslated("main"))
                        .option(createOption(INSTANCE.modEnabled)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(configTranslated("mob_effects"))
                                .option(createOption(INSTANCE.mobEffects.showItemTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .webpImage(image("mob_effect/example_item_tooltip.webp"))
                                                .text(configTranslatedDesc("mob_effects.show_item_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.showEffectsInInventoryTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .webpImage(image("mob_effect/example_effects_in_inventory_tooltip.webp"))
                                                .text(configTranslatedDesc("mob_effects.show_effects_in_inventory_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.showGuiTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .webpImage(image("mob_effect/example_gui_tooltip.webp"))
                                                .text(configTranslatedDesc("mob_effects.show_gui_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.showCommandCompletionTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.show_command_completion_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.extraDetailsInGuiTooltip)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.extra_details_in_gui_tooltip"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.extraDetailsInEffectsInInventoryTooltip)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.extra_details_in_effects_in_inventory_tooltip"))
                                                .build())
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(configTranslated("music_discs"))
                                .option(createOption(INSTANCE.musicDiscs.showLengthTooltip)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("music_discs.show_length_tooltip"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.musicDiscs.showRedstoneOutputTooltip)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("music_discs.show_redstone_output_tooltip"))
                                                .build())
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(configTranslated("paintings"))
                                .option(createOption(INSTANCE.paintings.showPreviewTooltip)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("paintings.show_preview_tooltip"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.paintings.previewTooltipScale)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("paintings.preview_tooltip_scale"))
                                                .build())
                                        .build())
                                .build()
                        )
                        .build())
                .save(DescriptorConfig::save)
                .build()
                .generateScreen(parent);
    }

    private static Component configTranslatedDesc(String key) {
        return Component.translatable(String.format("config.%s.%s.desc", Descriptor.MODID, key));
    }

    private static Identifier image(String path) {
        return Descriptor.id("textures/images/" + path);
    }

    private static Option.Builder<?> createOption(AbstractOption<?> configOption) {
        switch (configOption) {
            case BooleanOption booleanOption -> {
                return Option.<Boolean>createBuilder()
                        .name(booleanOption.getTitle())
                        .binding(booleanOption.getDefaultValue(), booleanOption::getValue, booleanOption::setValue)
                        .controller(TickBoxControllerBuilder::create);
            }
            case FloatOption floatOption -> {
                return Option.<Float>createBuilder()
                        .name(floatOption.getTitle())
                        .binding(floatOption.getDefaultValue(), floatOption::getValue, floatOption::setValue)
                        .controller(getFloatController(floatOption));
            }
            default -> throw new IllegalArgumentException("Unexpected value");
        }
    }

    private static Function<Option<Float>, ControllerBuilder<Float>> getFloatController(FloatOption floatOption) {
        return switch (floatOption.controller) {
            case Slider -> opt -> FloatSliderControllerBuilder.create(opt)
                    .step(0.01f)
                    .range(floatOption.minValue, floatOption.maxValue);
            case Field -> opt -> FloatFieldControllerBuilder.create(opt)
                    .range(floatOption.minValue, floatOption.maxValue);
        };
    }
}
