package com.minhhjjj.efprogressivediff.command;

import java.util.Collection;

import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import com.minhhjjj.efprogressivediff.event.MobEvents;
import com.minhhjjj.efprogressivediff.event.PlayerEvents;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class DifficultyCommand {
    @SuppressWarnings("null")
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("efpd").requires((source) -> source.hasPermission(2))
                .then(Commands.literal("add")
                    .then(Commands.argument("players", EntityArgument.players())
                    .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                    .executes(context -> {
                        double amount = DoubleArgumentType.getDouble(context, "amount");
                        int[] counter = {0};
                        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
                        for (ServerPlayer player : players) {
                            player.getData(PlayerDataAttachment.PLAYER_DIFFICULTY).addDifficulty(amount);
                            PlayerEvents.syncDifficulty(player);
                            player.sendSystemMessage(Component.translatable("command.efprogressivediff.add.notify", amount));
                            counter[0]++;
                        }
                        if (counter[0] == 0) {
                            context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.notfound"));
                        } else {
                            Entity sourceEntity = context.getSource().getEntity();
                            if (counter[0] == 1 && sourceEntity == players.iterator().next()) {
                                return 1;
                            } else {
                            context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.add.success", amount, counter[0]));
                            }
                        }
                        return counter[0];
                    }))
                    )
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0.0D, PDConfig.maxDifficultyCap))
                            .executes(context -> {
                                double amount = DoubleArgumentType.getDouble(context, "amount");
                                int[] counter = {0};
                                Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
                                for (ServerPlayer player : players) {
                                    player.getData(PlayerDataAttachment.PLAYER_DIFFICULTY).setDifficulty(amount);
                                    PlayerEvents.syncDifficulty(player);
                                    player.sendSystemMessage(Component.translatable("command.efprogressivediff.add.notify", amount));
                                    counter[0]++;
                                }
                                if (counter[0] == 0) {
                                    context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.notfound"));
                                } else {
                                    Entity sourceEntity = context.getSource().getEntity();
                                    if (counter[0] == 1 && sourceEntity == players.iterator().next()) {
                                        return 1;
                                    } else {
                                    context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.set.success", amount, counter[0]));
                                    }
                                }
                                return counter[0];
                            })
                        )
                    )
                )
                .then(Commands.literal("get")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            double difficulty = player.getData(PlayerDataAttachment.PLAYER_DIFFICULTY).getDifficulty();
                            context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.get.success", player.getName().getString(), difficulty));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("around")
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        double difficultyAround = MobEvents.getDifficultyAround(player);
                        if (difficultyAround < 0) {
                            context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.around.fail"));
                            return 0;
                        }
                        else context.getSource().sendSystemMessage(Component.translatable("command.efprogressivediff.around.success", difficultyAround));
                        return 1;
                    })
                )
        );
    }
}
