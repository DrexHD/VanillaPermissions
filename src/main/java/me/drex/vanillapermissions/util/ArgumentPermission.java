package me.drex.vanillapermissions.util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static me.drex.vanillapermissions.VanillaPermissionsMod.LOGGER;
import static me.drex.vanillapermissions.util.Permission.build;
import static me.drex.vanillapermissions.util.Permission.SELECTOR_ENTITY;
import static me.drex.vanillapermissions.util.Permission.SELECTOR_PLAYER;
import static me.drex.vanillapermissions.util.Permission.SELECTOR_SELF;
import static me.drex.vanillapermissions.util.Permission.SELECTOR_LIMIT;
import static me.drex.vanillapermissions.util.Permission.SELECTOR_WEIGHT;
import static me.lucko.fabric.api.permissions.v0.Options.get;
import static me.lucko.fabric.api.permissions.v0.Permissions.check;
import static net.minecraft.commands.arguments.EntityArgument.ERROR_SELECTORS_NOT_ALLOWED;

import static java.util.concurrent.CompletableFuture.allOf;

public class ArgumentPermission {

    private static void streamThrow() throws RuntimeException {
        throw new RuntimeException(ERROR_SELECTORS_NOT_ALLOWED.create());
    }

    public static void validate(CommandContext<CommandSourceStack> context, Collection<?> selected) throws CommandSyntaxException {
        var source = context.getSource();
        if (!source.isPlayer()) return;

        var name = build(source.getServer().getCommands().getDispatcher().getPath(context.getNodes().getLast().getNode()).toArray(String[]::new)); // > 1.21: source.getServer().getCommands().getDispatcher() -> source.dispatcher()
        var limit = get(source, SELECTOR_LIMIT.formatted(name), Integer::parseInt);
        if (limit.isPresent() && limit.get() < selected.size()) throw ERROR_SELECTORS_NOT_ALLOWED.create();

        var entity = check(source, SELECTOR_ENTITY.formatted(name), true);
        var player = check(source, SELECTOR_PLAYER.formatted(name), true);
        var self = check(source, SELECTOR_SELF.formatted(name), true);

        var weight = SELECTOR_WEIGHT.formatted(name);
        var sourceWeight = get(source, weight, Integer::parseInt);
        var sourceWeightPresent = sourceWeight.isPresent();
        if (entity && player && self && !sourceWeightPresent) return;
        var sourceWeightValue = sourceWeight.orElse(0);

        var sourcePlayer = source.getPlayer().getGameProfile();
        try {
            allOf(selected.parallelStream().mapMulti((object, consumer) -> {
                var selectedEntity = object;
                if (selectedEntity instanceof Player selectedPlayer) {
                    selectedEntity = selectedPlayer.getGameProfile();
                }

                if (selectedEntity instanceof Entity) {
                    if (!entity) streamThrow();
                } else if (selectedEntity instanceof GameProfile selectedPlayer) {
                    if (selectedPlayer.equals(sourcePlayer)) {
                        if (!self) streamThrow();
                    } else {
                        if (!player) streamThrow();
                        if (!sourceWeightPresent) return;

                        consumer.accept(get(selectedPlayer, weight, Integer::parseInt).thenAcceptAsync(selectedWeight -> {
                            if (selectedWeight.isPresent() && selectedWeight.get() > sourceWeightValue) throw new CompletionException(ERROR_SELECTORS_NOT_ALLOWED.create());
                        }));
                    }
                } else streamThrow();
            }).toArray(CompletableFuture[]::new)).get();
        } catch (Exception e) {
            if (e.getCause() instanceof CommandSyntaxException exception) throw exception;
            LOGGER.warn("Bad selector in command " + name, e);
            throw ERROR_SELECTORS_NOT_ALLOWED.create();
        }
    }
}
