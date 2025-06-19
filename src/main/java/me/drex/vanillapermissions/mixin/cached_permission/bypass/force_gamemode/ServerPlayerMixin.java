package me.drex.vanillapermissions.mixin.cached_permission.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.util.JoinCache;
import me.drex.vanillapermissions.util.Permission;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    //? if >= 1.21.6 {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }
    //?} else {
    /*public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }
    
    *///?}

    @WrapOperation(
        method = "calculateGameModeForNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;getForcedGameType()Lnet/minecraft/world/level/GameType;"
        )
    )
    public GameType addDefaultGameModeOverridePermission(MinecraftServer minecraftServer, Operation<GameType> original) {
        if (JoinCache.getCachedPermissions(uuid, Permission.BYPASS_FORCE_GAMEMODE).get()) {
            return null;
        }
        return original.call(minecraftServer);
    }

}
