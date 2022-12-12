package me.drex.vanillapermissions.mc119.mixin.adminbroadcast;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)
@Mixin(CommandSourceStack.class)
public abstract class CommandSourceStackMixin {

    @Unique
    private ServerPlayer vanillaPermissions_localServerPlayer;

    @Inject(
            method = "broadcastToAdmins",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void vanillaPermissions_captureServerPlayer(Component component, CallbackInfo ci, Component component2, Iterator var3, ServerPlayer serverPlayer) {
        vanillaPermissions_localServerPlayer = serverPlayer;
    }

    @ModifyExpressionValue(
            method = "broadcastToAdmins",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            )
    )
    public boolean vanillaPermissions_addAdminBroadcastReceivePermission(boolean original) {
        return Permissions.check(vanillaPermissions_localServerPlayer, Permission.ADMIN_BROADCAST_RECEIVE, original);
    }

}
