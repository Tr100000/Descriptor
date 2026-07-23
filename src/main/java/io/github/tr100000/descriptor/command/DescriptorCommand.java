package io.github.tr100000.descriptor.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.MobEffectHandlers;
import io.github.tr100000.descriptor.api.MobEffectHandlerType;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public final class DescriptorCommand {
    private DescriptorCommand() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(literal(Descriptor.MODID)
                .then(literal("mob_effect")
                        .then(literal("validate")
                                .executes(DescriptorCommand::validateMobEffectTranslations)
                        )
                        .then(literal("handler")
                                .then(argument("effect", ResourceArgument.resource(buildContext, Registries.MOB_EFFECT))
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

    @SuppressWarnings("unchecked")
    private static int printMobEffectHandler(CommandContext<FabricClientCommandSource> context) {
        Holder.Reference<MobEffect> effect = context.getArgument("effect", Holder.Reference.class);
        assert effect.key().isFor(Registries.MOB_EFFECT);

        MobEffectHandlerType<?> type = MobEffectHandlers.INSTANCE.get(effect).getType();
        Identifier handlerId = MobEffectHandlers.getHandlerId(type);
        context.getSource().sendFeedback(Component.translatable("command.descriptor.mob_effect.handler", effect.key().identifier(), handlerId));
        return 0;
    }

    private static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        DescriptorConfig.load();
        context.getSource().sendFeedback(Component.translatable("command.descriptor.config.reload"));
        return 0;
    }
}
