package me.drex.vanillapermissions.util;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.vanillapermissions.VanillaPermissionsMod;
import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ArgumentPermission {

    public static void check(CommandContext<CommandSourceStack> context, String selector, Collection<?> selected) throws CommandSyntaxException {
        var source = context.getSource();
        if (!source.isPlayer()) return;

        var root = context.getRootNode().getName();
        var limit = Options.get(source, Permission.SELECTOR_LIMIT.formatted(root, selector), Integer::parseInt);
        if (limit.isPresent() && limit.get() < selected.size()) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();

        var entity = Permissions.check(source, Permission.SELECTOR_ENTITY.formatted(root, selector), true);
        var player = Permissions.check(source, Permission.SELECTOR_PLAYER.formatted(root, selector), true);
        var self = Permissions.check(source, Permission.SELECTOR_SELF.formatted(root, selector), true);
        if (entity && player && self) return;

        var weight = Permission.SELECTOR_WEIGHT.formatted(root, selector);
        var sourceWeight = Options.get(source, weight, Integer::parseInt);
        var sourceWeightPresent = sourceWeight.isPresent();
        var sourceWeightValue = sourceWeight.orElse(0);

        var weightChecks = new CompletableFuture[selected.size()];
        var weightCheckIndex = 0;

        var sourcePlayer = source.getPlayer().getGameProfile();
        for (var selectedEntity : selected) {
            if (selectedEntity instanceof Player selectedPlayer) {
                selectedEntity = selectedPlayer.getGameProfile();
            }

            if (selectedEntity instanceof Entity) {
                if (!entity) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
            } else if (selectedEntity instanceof GameProfile selectedPlayer) {
                if (selectedPlayer.equals(sourcePlayer)) {
                    if (!self) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
                } else {
                    if (!player) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
                    if (!sourceWeightPresent) continue;

                    weightChecks[weightCheckIndex] = Options.get(selectedPlayer, weight, Integer::parseInt).thenAcceptAsync(selectedWeight -> {
                        if (selectedWeight.isPresent() && selectedWeight.get() > sourceWeightValue) throw new CompletionException(EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create());
                    });
                    weightCheckIndex++;
                }
            } else {
                throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
            }
        }

        if (!sourceWeightPresent) return;
        try {
            CompletableFuture.allOf(weightChecks).get();
        } catch (Exception e) {
            if (e.getCause() instanceof CommandSyntaxException exception) throw exception;
            VanillaPermissionsMod.LOGGER.warn("Bad selector: {}", e);
            throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
        }
    }
}
