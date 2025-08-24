package com.fireblaze.stamina_mod.commands;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import com.fireblaze.stamina_mod.comfort.ComfortHelper;
import com.fireblaze.stamina_mod.config.Settings;

import java.util.Collection;

public class StaminaCommands {

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 20;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("stamina")
                        .requires(source -> source.hasPermission(2)) // nur OPs

                        // ===== LEVEL =====
                        .then(Commands.literal("set")
                                .then(Commands.literal("level")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(MIN_LEVEL, MAX_LEVEL))
                                                .executes(ctx -> setLevel(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "value"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return setLevel(targets, value, ctx.getSource());
                                                        }))
                                        )))

                        .then(Commands.literal("add")
                                .then(Commands.literal("level")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> addLevel(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "amount"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return addLevel(targets, amount, ctx.getSource());
                                                        }))
                                        )))

                        .then(Commands.literal("remove")
                                .then(Commands.literal("level")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> removeLevel(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "amount"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return removeLevel(targets, amount, ctx.getSource());
                                                        }))
                                        )))

                        // ===== EXPERIENCE =====
                        .then(Commands.literal("set")
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                .executes(ctx -> setExp(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "value"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return setExp(targets, value, ctx.getSource());
                                                        }))
                                        )))

                        .then(Commands.literal("add")
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> addExp(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "amount"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return addExp(targets, amount, ctx.getSource());
                                                        }))
                                        )))

                        .then(Commands.literal("remove")
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> removeExp(ctx.getSource().getPlayerOrException(),
                                                        IntegerArgumentType.getInteger(ctx, "amount"),
                                                        ctx.getSource()))
                                                .then(Commands.argument("targets", EntityArgument.players())
                                                        .executes(ctx -> {
                                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                            return removeExp(targets, amount, ctx.getSource());
                                                        }))
                                        )))
        );

        dispatcher.register(
                Commands.literal("comfort_reset")
                        .requires(source -> source.hasPermission(2)) // nur OPs
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ServerLevel level = player.serverLevel();
                            ComfortHelper.resetHighlightedBlocks(level);
                            ctx.getSource().sendSuccess(() -> Component.literal("Highlighted blocks reset!"), true);
                            return Command.SINGLE_SUCCESS;
                        })
        );
    }

    // ===== LEVEL METHODS =====
    private static int setLevel(ServerPlayer player, int value, CommandSourceStack source) {
        return setLevel(java.util.List.of(player), value, source);
    }

    private static int setLevel(Collection<ServerPlayer> players, int value, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                stamina.setStaminaLvl(value);
                stamina.setStaminaExp(0.0f);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Level set to " + value), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int addLevel(ServerPlayer player, int amount, CommandSourceStack source) {
        return addLevel(java.util.List.of(player), amount, source);
    }

    private static int addLevel(Collection<ServerPlayer> players, int amount, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                int newLevel = Math.min(MAX_LEVEL, stamina.getStaminaLvl() + amount);
                stamina.setStaminaLvl(newLevel);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Level increased by " + amount + " (now: " + newLevel + ")"), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int removeLevel(ServerPlayer player, int amount, CommandSourceStack source) {
        return removeLevel(java.util.List.of(player), amount, source);
    }

    private static int removeLevel(Collection<ServerPlayer> players, int amount, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                int newLevel = Math.max(MIN_LEVEL, stamina.getStaminaLvl() - amount);
                stamina.setStaminaLvl(newLevel);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Level decreased by " + amount + " (now: " + newLevel + ")"), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    // ===== EXPERIENCE METHODS =====
    private static int setExp(ServerPlayer player, int value, CommandSourceStack source) {
        return setExp(java.util.List.of(player), value, source);
    }

    private static int setExp(Collection<ServerPlayer> players, int value, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                stamina.setStaminaExp((float) value);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Experience set to " + value), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int addExp(ServerPlayer player, int amount, CommandSourceStack source) {
        return addExp(java.util.List.of(player), amount, source);
    }

    private static int addExp(Collection<ServerPlayer> players, int amount, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                float newExp = stamina.getStaminaExp() + amount;
                stamina.setStaminaExp(newExp);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Experience increased by " + amount + " (now: " + newExp + ")"), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int removeExp(ServerPlayer player, int amount, CommandSourceStack source) {
        return removeExp(java.util.List.of(player), amount, source);
    }

    private static int removeExp(Collection<ServerPlayer> players, int amount, CommandSourceStack source) {
        for (ServerPlayer player : players) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                float newExp = Math.max(0, stamina.getStaminaExp() - amount);
                stamina.setStaminaExp(newExp);
                source.sendSuccess(() -> Component.literal(
                        player.getName().getString() + " Stamina Experience decreased by " + amount + " (now: " + newExp + ")"), true);
            });
        }
        return Command.SINGLE_SUCCESS;
    }
}
