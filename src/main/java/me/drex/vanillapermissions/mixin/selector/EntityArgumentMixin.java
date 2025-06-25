package me.drex.vanillapermissions.mixin.selector;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityArgument.class)
public abstract class EntityArgumentMixin {

    private static CommandSyntaxException NotAllowedException = EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();

    private static boolean canSelectEntity(CommandSourceStack source, String root, String selector) {
        return Permissions.check(source, Permission.SELECTOR_ENTITY.formatted(root, selector), true);
    }

    private static boolean canSelectPlayer(CommandSourceStack source, String root, String selector) {
        return Permissions.check(source, Permission.SELECTOR_PLAYER.formatted(root, selector), true);
    }

    private static boolean canSelectSelf(CommandSourceStack source, String root, String selector) {
        return Permissions.check(source, Permission.SELECTOR_SELF.formatted(root, selector), true);
    }

    @Inject(
        method = {
            "getEntity",
            "getPlayer",
        },
        at = @At("RETURN")
    )
    private static void addEntityPermission(CommandContext<CommandSourceStack> commandContext, String string, CallbackInfoReturnable<Entity> cir) throws CommandSyntaxException {
        var source = commandContext.getSource();
        if (!source.isPlayer()) return;

        var root = commandContext.getRootNode().getName();
        if (canSelectEntity(source, root, string)) return;

        var selected = cir.getReturnValue();
        if (!(selected instanceof ServerPlayer)) throw NotAllowedException;

        if (canSelectPlayer(source, root, string) || source.getPlayer() == selected && canSelectSelf(source, root, string)) return;

        throw NotAllowedException;
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
        var source = commandContext.getSource();
        if (!source.isPlayer()) return;

        var root = commandContext.getRootNode().getName();
        if (canSelectEntity(source, root, string)) return;

        var self = source.getPlayer();
        var selfOnly = true;
        for (var selected : cir.getReturnValue()) {
            if (!(selected instanceof ServerPlayer)) throw NotAllowedException;
            if (selected != self) {
                selfOnly = false;
            }
        }

        if (canSelectPlayer(source, root, string) || selfOnly && canSelectSelf(source, root, string)) return;

        throw NotAllowedException;
    }
}
