package io.github.tr100000.descriptor.mixin.mob_effects;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import io.github.tr100000.descriptor.gui.MessageWithCustomTooltip;
import io.github.tr100000.descriptor.gui.MobEffectDescriptionTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(SharedSuggestionProvider.class)
public interface SharedSuggestionProviderMixin {
    @Shadow
    static <T> void filterResources(final Iterable<T> values, final String contents, final Function<T, Identifier> converter, final Consumer<T> consumer) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(
            method = "suggestRegistryElements(Lnet/minecraft/core/HolderLookup;Lnet/minecraft/commands/SharedSuggestionProvider$ElementSuggestionType;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;Ljava/util/function/Predicate;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/SharedSuggestionProvider;suggestResource(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;"),
            cancellable = true
    )
    private <E> void suggestRegistryElements(HolderLookup<E> registry, SharedSuggestionProvider.ElementSuggestionType elements, SuggestionsBuilder builder, Predicate<E> filter, CallbackInfo ci) {
        if (registry == BuiltInRegistries.MOB_EFFECT && DescriptorConfig.check(c -> c.mobEffects.showCommandCompletionTooltips)) {
            ci.cancel();

            Iterable<Identifier> values = registry.listElementIds().map(ResourceKey::identifier)::iterator;
            String contents = builder.getRemaining().toLowerCase(Locale.ROOT);
            filterResources(values, contents, Function.identity(), v -> suggestMobEffectWithDescription(v, builder));
        }
    }

    @Unique
    private void suggestMobEffectWithDescription(Identifier id, final SuggestionsBuilder builder) {
        Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.get(id).orElseThrow();
        ClientTooltipComponent tooltipComponent = new MobEffectDescriptionTooltipComponent(id, effectHolder);
        Message message = (MessageWithCustomTooltip)() -> List.of(tooltipComponent);
        builder.suggest(id.toString(), message);
    }
}
