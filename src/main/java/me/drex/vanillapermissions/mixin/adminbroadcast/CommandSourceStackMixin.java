package me.drex.vanillapermissions.mixin.adminbroadcast;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin {

    @ModifyExpressionValue(
        method = "broadcastToAdmins",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
        )
    )
    public boolean addAdminBroadcastReceivePermission(boolean original, @Local ServerPlayer player) {
        return Permissions.check(player, Permission.ADMIN_BROADCAST_RECEIVE, original);
    }

}

