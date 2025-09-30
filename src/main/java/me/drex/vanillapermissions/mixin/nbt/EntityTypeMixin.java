package me.drex.vanillapermissions.mixin.nbt;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {

    @ModifyExpressionValue(
        method = "updateCustomEntityTag",
        at = @At(
            value = "INVOKE",
            //? if >= 1.21.9-rc1 {
            target = "Lnet/minecraft/server/players/PlayerList;isOp(Lnet/minecraft/server/players/NameAndId;)Z"
            //?} else {
            /*target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            *///?}
        )
    )
    private static boolean addNbtLoadEntityPermission(boolean original, @Local Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return Permissions.check(serverPlayer, Permission.NBT_LOAD_ENTITY, original);
        }
        return original;
    }
}
