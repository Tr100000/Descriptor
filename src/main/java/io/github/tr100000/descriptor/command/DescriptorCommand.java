package io.github.tr100000.descriptor.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.DescriptorUtil;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public final class DescriptorCommand {
    private DescriptorCommand() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(literal(Descriptor.MODID)
                .then(literal("check")
                        .then(literal("mob_effect")
                                .executes(DescriptorCommand::checkMobEffectTranslations)
                        )
                )
                .then(literal("config")
                        .then(literal("reload")
                                .executes(DescriptorCommand::reloadConfig)
                        )
                )
        );
    }

    private static int checkMobEffectTranslations(CommandContext<FabricClientCommandSource> context) {
        int missingCount = 0;
        for (Identifier key : BuiltInRegistries.MOB_EFFECT.keySet()) {
            if (!I18n.exists(DescriptorUtil.getMobEffectDescriptionKey(key))) {
                context.getSource().sendFeedback(Component.translatable("command.descriptor.check.mob_effect.missing", key));
                missingCount++;
            }
        }

        if (missingCount == 0)
            context.getSource().sendFeedback(Component.translatable("command.descriptor.check.mob_effect.all_present"));

        return missingCount;
    }

    private static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        DescriptorConfig.load();
        context.getSource().sendFeedback(Component.translatable("command.descriptor.config.reload"));
        return 0;
    }
}
