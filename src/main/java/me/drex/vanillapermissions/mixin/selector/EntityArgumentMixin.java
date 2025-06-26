package me.drex.vanillapermissions.mixin.selector;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;

@Mixin(EntityArgument.class)
public abstract class EntityArgumentMixin {

    private static void addSelectorPermission(CommandContext<CommandSourceStack> context, String selector, Collection<Entity> selected) throws CommandSyntaxException {
        var source = context.getSource();
        if (!source.isPlayer()) return;

        var root = context.getRootNode().getName();
        var limit = Options.get(source, Permission.SELECTOR_LIMIT.formatted(root, selector), Integer::parseInt);
        if (limit.isPresent() && limit.get() < selected.size()) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();

        var weight = Permission.SELECTOR_WEIGHT.formatted(root, selector);
        var sourceWeight = Options.get(source, weight, Integer::parseInt);

        var sourceWeightPresent = sourceWeight.isPresent();
        var sourceWeightValue = 0;

        if (sourceWeightPresent) {
            sourceWeightValue = sourceWeight.get();
        }

        var entity = Permissions.check(source, Permission.SELECTOR_ENTITY.formatted(root, selector), true);
        var player = Permissions.check(source, Permission.SELECTOR_PLAYER.formatted(root, selector), true);
        var self = Permissions.check(source, Permission.SELECTOR_SELF.formatted(root, selector), true);

        var sourcePlayer = source.getPlayer();
        for (var selectedEntity : selected) {
            if (selectedEntity instanceof ServerPlayer) {
                if (selectedEntity == sourcePlayer) {
                    if (!self) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
                } else {
                    if (!player) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
                }
            } else {
                if (!entity) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
            }

            if (sourceWeightPresent) {
                var selectedWeight = Options.get(selectedEntity, weight, Integer::parseInt);
                if (selectedWeight.isPresent() && selectedWeight.get() > sourceWeightValue) throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
            }
        }
    }

    @Inject(
        method = {
            "getEntity",
            "getPlayer",
        },
        at = @At("RETURN")
    )
    private static void addEntityPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Entity> cir) throws CommandSyntaxException {
        addSelectorPermission(commandContext, string, Collections.singleton(cir.getReturnValue()));
    }

    @Inject(
        method = {
            "getEntities",
            "getOptionalEntities",
            "getPlayers",
            "getOptionalPlayers",
        },
        at = @At("RETURN")
    )
    private static void addEntitiesPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Collection<Entity>> cir) throws CommandSyntaxException {
        addSelectorPermission(commandContext, string, cir.getReturnValue());
    }
}
