package com.minhhjjj.efprogressivediff.command;

import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
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

public class DifficultyCommand {
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
                        for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                            player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                                cap.addDifficulty(amount);
                                PlayerEvents.syncDifficulty(player);
                                player.sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Adding " + amount + " to your difficulty"));
                                counter[0]++;
                            });
                        }
                        if (counter[0] == 0) {
                            context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] No players found to add difficulty."));
                        } else {
                            context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Added " + amount + " difficulty to " + counter[0] + " player(s)."));
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
                                for (ServerPlayer player : EntityArgument.getPlayers(context, "players")) {
                                    player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                                        cap.setDifficulty(amount);
                                        PlayerEvents.syncDifficulty(player);
                                        player.sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Your difficulty has been set to " + amount));
                                        counter[0]++;
                                    });
                                }
                                if (counter[0] == 0) {
                                    context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] No players found to set difficulty."));
                                } else {
                                    context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Set difficulty to " + amount + " for " + counter[0] + " player(s)."));
                                }
                                return counter[0];
                            })
                        )
                    )
                )
                .then(Commands.literal("get")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            int[] hasCap = {0};
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                                double difficulty = cap.getDifficulty();
                                context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] " + player.getName().getString() + "'s difficulty is " + difficulty));
                                hasCap[0] = 1;
                            });
                            if (hasCap[0] == 0) {
                                context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Unable to retrieve difficulty for player " + player.getName().getString()));
                            }
                            return hasCap[0];
                        })
                    )
                )
                .then(Commands.literal("around")
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        double difficultyAround = MobEvents.getDifficultyAround(player);
                        if (difficultyAround < 0) {
                            context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] No nearby players with difficulty data found."));
                            return 0;
                        }
                        else context.getSource().sendSystemMessage(Component.literal("[Epic Fight Progressive Difficulty] Around difficulty is " + difficultyAround));
                        return 1;
                    })
                )
        );
    }
}
