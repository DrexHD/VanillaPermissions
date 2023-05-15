package me.drex.vanillapermissions.mixin.selector;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.context.CommandContext;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityArgument.class)
public abstract class EntityArgumentMixin {

    @ModifyExpressionValue(
            method = "listSuggestions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/commands/SharedSuggestionProvider;hasPermission(I)Z"
            )
    )
    public boolean vanillaPermissions_addSelectorPermission(boolean original, CommandContext<SharedSuggestionProvider> context) {
        return Permissions.check(context.getSource(), Permission.SELECTOR, original);
    }

}
