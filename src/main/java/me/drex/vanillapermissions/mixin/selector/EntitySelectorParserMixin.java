package me.drex.vanillapermissions.mixin.selector;
//? if >= 1.21.1 {
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelectorParser.class)
public abstract class EntitySelectorParserMixin {

    @ModifyExpressionValue(
        method = "allowSelectors",
        at = @At(
            value = "INVOKE",
            //? if > 1.21.10 {
            target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"
            //? } else if >= 1.21.6 {
            /*target = "Lnet/minecraft/commands/CommandSourceStack;allowsSelectors()Z"
            *///?} else {
            /*target = "Lnet/minecraft/commands/SharedSuggestionProvider;hasPermission(I)Z"
            *///?}
        )
    )
    private static <S> boolean addSelectorPermission(boolean original, S object) {
        if (object instanceof CommandSourceStack source) {
            return Permissions.check(source, Permission.SELECTOR, original);
        }
        return original;
    }

}
//?} else {

/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class EntitySelectorParserMixin {

}
*///?}
