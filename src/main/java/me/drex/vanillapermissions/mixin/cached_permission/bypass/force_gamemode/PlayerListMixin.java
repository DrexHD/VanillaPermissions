package me.drex.vanillapermissions.mixin.cached_permission.bypass.force_gamemode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Arguments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @WrapOperation(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;loadGameTypes(Lnet/minecraft/nbt/CompoundTag;)V"
        )
    )
    public void addConnectionArgument(ServerPlayer instance, CompoundTag compoundTag, Operation<Void> original, @Local(argsOnly = true) Connection connection) {
        Connection previous = Arguments.CONNECTION.get();
        Arguments.CONNECTION.set(connection);
        try {
            original.call(instance, compoundTag);
        } finally {
            Arguments.CONNECTION.set(previous);
        }
    }
}
