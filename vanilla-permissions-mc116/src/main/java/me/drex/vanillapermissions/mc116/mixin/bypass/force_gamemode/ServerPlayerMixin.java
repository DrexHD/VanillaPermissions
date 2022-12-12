package me.drex.vanillapermissions.mc116.mixin.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = "<1.17-"
        )
)
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @WrapOperation(
            method = "readAdditionalSaveData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getForceGameType()Z"
            )
    )
    public boolean fabricpermissions_addDefaultGameModeOverridePermission(MinecraftServer minecraftServer, Operation<Boolean> original) {
        if (Permissions.check((ServerPlayer) (Object) this, Permission.BYPASS_FORCE_GAMEMODE)) {
            return false;
        } else {
            return original.call(minecraftServer);
        }
    }

}