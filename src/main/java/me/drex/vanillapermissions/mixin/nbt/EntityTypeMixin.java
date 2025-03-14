package me.drex.vanillapermissions.mixin.nbt;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {

    @ModifyExpressionValue(
            method = "updateCustomEntityTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            )
    )
    private static boolean vanillaPermissions_addNbtLoadEntityPermission(boolean original, Level level, LivingEntity player) {
        return Permissions.check(player, Permission.NBT_LOAD_ENTITY, original);
    }
}
