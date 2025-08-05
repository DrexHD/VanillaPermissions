package me.drex.vanillapermissions.util;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.RootCommandNode;
import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static me.drex.vanillapermissions.VanillaPermissionsMod.LOGGER;
import static me.drex.vanillapermissions.util.Permission.*;
import static net.minecraft.commands.arguments.EntityArgument.ERROR_SELECTORS_NOT_ALLOWED;

public class ArgumentPermission {

    public static void validate(CommandContext<CommandSourceStack> context, String selector, Collection<?> selected) throws CommandSyntaxException {
        var source = context.getSource();
        if (!source.isPlayer()) return;

        String[] parts;
        if (context.getRootNode() instanceof RootCommandNode) {
            parts = context.getNodes().stream().map(node -> node.getNode().getName()).toArray(String[]::new);
        } else {
            parts = source.getServer().getCommands().getDispatcher().getPath(Iterables.getLast(context.getNodes()).getNode()).toArray(String[]::new);
        }
        var name = Permission.build(parts[0], selector, Permission.build(1, parts.length, parts));

        var limit = Options.get(source, SELECTOR_LIMIT.formatted(name), Integer::parseInt);
        if (limit.isPresent() && limit.get() < selected.size()) throw ERROR_SELECTORS_NOT_ALLOWED.create();

        var entity = Permissions.check(source, SELECTOR_ENTITY.formatted(name), true);
        var player = Permissions.check(source, SELECTOR_PLAYER.formatted(name), true);
        var self = Permissions.check(source, SELECTOR_SELF.formatted(name), true);

        var weight = SELECTOR_WEIGHT.formatted(name);
        var sourceWeight = Options.get(source, weight, Integer::parseInt);
        var sourceWeightPresent = sourceWeight.isPresent();
        if (entity && player && self && !sourceWeightPresent) return;
        var sourceWeightValue = sourceWeight.orElse(0);

        var sourcePlayer = source.getPlayer().getGameProfile();
        try {
            CompletableFuture.allOf(selected.stream().mapMulti((object, consumer) -> {
                var selectedEntity = object;
                if (selectedEntity instanceof Player selectedPlayer) {
                    selectedEntity = selectedPlayer.getGameProfile();
                }

                if (selectedEntity instanceof Entity) {
                    if (!entity) throwSelectorError();
                } else if (selectedEntity instanceof GameProfile selectedPlayer) {
                    if (selectedPlayer.equals(sourcePlayer)) {
                        if (!self) throwSelectorError();
                    } else {
                        if (!player) throwSelectorError();
                        if (!sourceWeightPresent) return;

                        //? if >= 1.21.6 {
                        consumer.accept(Options.get(selectedPlayer, weight, Integer::parseInt).thenAcceptAsync(selectedWeight -> {
                            if (selectedWeight.isPresent() && selectedWeight.get() > sourceWeightValue) {
                                throw new CompletionException(ERROR_SELECTORS_NOT_ALLOWED.create());
                            }
                        }));
                        //?} else {
                        /*if (object instanceof Player onlinePlayer) {
                            var selectedWeight = Options.get(onlinePlayer, weight, Integer::parseInt);
                            if (selectedWeight.isPresent() && selectedWeight.get() > sourceWeightValue) {
                                consumer.accept(CompletableFuture.failedFuture(new CompletionException(ERROR_SELECTORS_NOT_ALLOWED.create())));
                            }
                        }
                        *///?}
                    }
                } else throwSelectorError();
            }).unordered().toArray(CompletableFuture[]::new)).get();
        } catch (Exception e) {
            if (e.getCause() instanceof CommandSyntaxException exception) throw exception;
            LOGGER.warn("Bad selector in command {}", name, e);
            throw ERROR_SELECTORS_NOT_ALLOWED.create();
        }
    }

    private static void throwSelectorError() throws RuntimeException {
        throw new RuntimeException(ERROR_SELECTORS_NOT_ALLOWED.create());
    }
}
