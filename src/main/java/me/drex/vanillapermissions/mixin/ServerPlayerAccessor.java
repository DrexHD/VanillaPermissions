package me.drex.vanillapermissions.mixin;

import net.minecraft.server.level.ServerPlayer;
//? if > 1.21.10 {
import net.minecraft.server.permissions.PermissionSet;
//? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {

    //? if > 1.21.10 {
    @Invoker
    PermissionSet invokePermissions();
    //? } else {
    /*@Invoker
    int invokeGetPermissionLevel();
    *///? }

}
