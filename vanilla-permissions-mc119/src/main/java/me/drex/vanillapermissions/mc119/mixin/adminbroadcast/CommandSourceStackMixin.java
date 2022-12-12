package me.drex.vanillapermissions.mc119.mixin.adminbroadcast;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin {

    @ModifyExpressionValue(
            method = "broadcastToAdmins",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            )
    )
    public boolean vanillaPermissions_addAdminBroadcastReceivePermission(boolean original) {
        return Permissions.check((CommandSourceStack) (Object) this, Permission.ADMIN_BROADCAST_RECEIVE, original);
    }

}
