package me.drex.vanillapermissions.mc119.mixin.selector;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.Constants;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin {

    @ModifyExpressionValue(
            method = "checkPermissions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/commands/CommandSourceStack;hasPermission(I)Z"
            )
    )
    public boolean vanillaPermissions_addSelectorPermission(boolean original, CommandSourceStack source) {
        return Permissions.check(source, Constants.SELECTOR, original);
    }

}
