package me.drex.vanillapermissions.mc119.mixin.nbt;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = @Condition(
                value = "minecraft",
                versionPredicates = ">=1.14"
        )
)@Mixin(EntityType.class)
public abstract class EntityTypeMixin {

    @ModifyExpressionValue(
            method = "updateCustomEntityTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;isOp(Lcom/mojang/authlib/GameProfile;)Z"
            )
    )
    private static boolean vanillaPermissions_addNbtLoadEntityPermission(boolean original, Level level, Player player) {
        return Permissions.check(player, Permission.NBT_LOAD_ENTITY, original);
    }
}
