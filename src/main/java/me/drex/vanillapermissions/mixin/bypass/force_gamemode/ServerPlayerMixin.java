package me.drex.vanillapermissions.mixin.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @WrapOperation(
            method = "calculateGameModeForNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getForcedGameType()Lnet/minecraft/world/level/GameType;"
            )
    )
    public GameType vanillaPermissions_addDefaultGameModeOverridePermission(MinecraftServer minecraftServer, Operation<GameType> original) {
        if (Permissions.check((ServerPlayer) (Object) this, Permission.BYPASS_FORCE_GAMEMODE)) {
            return null;
        } else {
            return original.call(minecraftServer);
        }
    }

}
