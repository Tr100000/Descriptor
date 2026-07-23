package io.github.tr100000.descriptor.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.MobEffectHandlers;
import io.github.tr100000.descriptor.api.MobEffectHandlerType;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

import java.util.List;
import java.util.Optional;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public final class DescriptorCommand {
    private DescriptorCommand() {}

    private static final SuggestionProvider<FabricClientCommandSource> MOB_EFFECT_SUGGESTIONS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(BuiltInRegistries.MOB_EFFECT.keySet(), builder);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(literal(Descriptor.MODID)
                .then(literal("mob_effect")
                        .then(literal("validate")
                                .executes(DescriptorCommand::validateMobEffectTranslations)
                        )
                        .then(literal("handler")
                                .then(argument("effect", IdentifierArgument.id())
                                        .suggests(MOB_EFFECT_SUGGESTIONS)
                                        .executes(DescriptorCommand::printMobEffectHandler)
                                )
                        )
                )
                .then(literal("config")
                        .then(literal("reload")
                                .executes(DescriptorCommand::reloadConfig)
                        )
                )
        );
    }

    private static int validateMobEffectTranslations(CommandContext<FabricClientCommandSource> context) {
        int invalidCount = 0;
        List<Component> errors = new ObjectArrayList<>();
        for (Holder<MobEffect> effect : BuiltInRegistries.MOB_EFFECT.listElements().toList()) {
            if (!MobEffectHandlers.INSTANCE.get(effect).validate(effect, errors::add)) {
                if (!errors.isEmpty()) {
                    context.getSource().sendError(Component.translatable("command.descriptor.mob_effect.validate.error", effect.unwrapKey().orElseThrow().identifier(), errors.size()));
                    errors.forEach(context.getSource()::sendError);
                }
                else {
                    context.getSource().sendError(Component.translatable("command.descriptor.mob_effect.validate.error.no_details", effect.unwrapKey().orElseThrow().identifier()));
                }
                invalidCount++;
            }
            errors.clear();
        }

        if (invalidCount == 0)
            context.getSource().sendFeedback(Component.translatable("command.descriptor.mob_effect.validate.all_ok"));

        return invalidCount;
    }

    private static int printMobEffectHandler(CommandContext<FabricClientCommandSource> context) {
        Identifier effectId = context.getArgument("effect", Identifier.class);
        Optional<Holder.Reference<MobEffect>> effectOptional = BuiltInRegistries.MOB_EFFECT.get(effectId);
        if (effectOptional.isPresent()) {
            MobEffectHandlerType<?> type = MobEffectHandlers.INSTANCE.get(effectOptional.get()).getType();
            Identifier handlerId = MobEffectHandlers.getHandlerId(type);
            context.getSource().sendFeedback(Component.translatable("command.descriptor.mob_effect.handler", effectId, handlerId));
            return 0;
        }
        else {
            context.getSource().sendError(Component.translatable("command.descriptor.mob_effect.handler.error_doesnt_exist", effectId));
            return 1;
        }
    }

    private static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        DescriptorConfig.load();
        context.getSource().sendFeedback(Component.translatable("command.descriptor.config.reload"));
        return 0;
    }
}
