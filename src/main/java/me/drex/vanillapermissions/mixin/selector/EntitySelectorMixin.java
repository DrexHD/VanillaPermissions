package me.drex.vanillapermissions.mixin.selector;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    @ModifyExpressionValue(
        method = "checkPermissions",
        at = @At(
            value = "INVOKE",
            //? if >= 1.21.6 {
            target = "Lnet/minecraft/commands/CommandSourceStack;allowsSelectors()Z"
            //?} else {
            /*target = "Lnet/minecraft/commands/CommandSourceStack;hasPermission(I)Z"
            *///?}
        )
    )
    public boolean addSelectorPermission(boolean original, CommandSourceStack source) {
        return Permissions.check(source, Permission.SELECTOR, original);
    }

}
