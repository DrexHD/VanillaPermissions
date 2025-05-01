package me.drex.vanillapermissions.mixin.cached_permission.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.vanillapermissions.util.Arguments;
import me.drex.vanillapermissions.util.IConnection;
import me.drex.vanillapermissions.util.Permission;
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
        if (Arguments.CONNECTION.get() instanceof IConnection connection) {
            if (connection.vanillaPermissions$getCachedPermission(Permission.BYPASS_WHITELIST)) {
                return null;
            }
        }
        return original.call(minecraftServer);
    }

}
